"""
Console User Interfaces for MediQueue (User-Input Driven)
Patient Portal, Doctor Dashboard, Admin Panel, Main Menu
"""
import sys
from typing import Optional

from mediqueue.models import Department, Doctor, Patient
from mediqueue.patterns import HospitalDepartment
from mediqueue.utils import ConsoleColors, ConsolePrinter, DateTimeUtils


class PatientPortal:
    def __init__(self, general_dept: HospitalDepartment, emergency_dept: HospitalDepartment, vip_dept: HospitalDepartment):
        self.general_dept = general_dept
        self.emergency_dept = emergency_dept
        self.vip_dept = vip_dept
        self.current_patient: Optional[Patient] = None
        self.current_dept: Optional[HospitalDepartment] = None
        self.patient_counter = 100

    def show(self):
        running = True
        while running:
            self._print_portal_menu()
            choice = self._get_int_input()
            if choice == 1:
                self._register_patient()
            elif choice == 2:
                self._book_appointment()
            elif choice == 3:
                self._generate_token_and_admit()
            elif choice == 4:
                self._view_queue_status()
            elif choice == 5:
                self._check_wait_time()
            elif choice == 6:
                self._cancel_appointment()
            elif choice == 7:
                self._reschedule_appointment()
            elif choice == 8:
                self._view_my_details()
            elif choice == 9:
                running = False
            else:
                ConsolePrinter.print_error("Invalid choice. Please select an option between 1 and 9.")

    def _print_portal_menu(self):
        print(ConsoleColors.BOLD_GREEN)
        print("  -----------------------------------------------------------------")
        print("                       PATIENT PORTAL                              ")
        if self.current_patient:
            print(f"                  Welcome, {self.current_patient.name} [{self.current_patient.department.code}]")
        print("  -----------------------------------------------------------------")
        print(ConsoleColors.RESET)
        print(f"{ConsoleColors.BOLD_WHITE}   [1]  Register as New Patient")
        print("   [2]  Book Appointment")
        print("   [3]  Generate Token & Join Queue")
        print("   [4]  View Live Queue Status")
        print("   [5]  Check My Wait Time")
        print("   [6]  Cancel Appointment")
        print("   [7]  Reschedule Appointment")
        print("   [8]  View My Details")
        print(f"   [9]  Back to Main Menu{ConsoleColors.RESET}")
        print()

    def _register_patient(self):
        print(f"\n  {ConsoleColors.BOLD_GREEN}--- PATIENT REGISTRATION ---{ConsoleColors.RESET}")
        name = input("  >> Full Name    : ").strip()
        if not name:
            name = "Patient"
        age_str = input("  >> Age          : ").strip()
        try:
            age = int(age_str)
        except ValueError:
            age = 30
        contact = input("  >> Contact No   : ").strip() or "9999999999"
        email = input("  >> Email        : ").strip() or "patient@hospital.com"

        print("\n  Departments: [1] General  [2] Emergency  [3] VIP")
        dept_choice = self._get_int_input("  >> Department Choice : ")
        if dept_choice == 2:
            dept = Department.EMERGENCY
            target_dept = self.emergency_dept
            is_emergency = True
        elif dept_choice == 3:
            dept = Department.VIP
            target_dept = self.vip_dept
            is_emergency = False
        else:
            dept = Department.GENERAL
            target_dept = self.general_dept
            is_emergency = False

        self.patient_counter += 1
        pid = DateTimeUtils.generate_patient_id(self.patient_counter)
        patient = Patient(
            patient_id=pid,
            name=name,
            age=age,
            contact=contact,
            email=email,
            department=dept,
            is_emergency=is_emergency,
            registration_time=DateTimeUtils.get_current_timestamp()
        )
        target_dept.register_patient(patient)
        self.current_patient = patient
        self.current_dept = target_dept
        ConsolePrinter.print_success(f"Patient registered successfully! Assigned ID: {patient.patient_id} ({dept.display_name})")

    def _book_appointment(self):
        if not self.current_patient or not self.current_dept:
            ConsolePrinter.print_warning("Please register or select a patient profile first (Option [1]).")
            return
        doctors = self.current_dept.get_available_doctors()
        if not doctors:
            ConsolePrinter.print_warning(f"No available doctors in {self.current_dept.department_name}.")
            return
        print("\n  Available Doctors:")
        for idx, doc in enumerate(doctors):
            print(f"   [{idx + 1}] Dr. {doc.name} ({doc.specialization})")
        doc_choice = self._get_int_input("  >> Select Doctor : ")
        if 1 <= doc_choice <= len(doctors):
            selected_doc = doctors[doc_choice - 1]
            time_slot = input("  >> Preferred Time Slot (e.g. 10:30 AM / IMMEDIATE) : ").strip() or "11:00 AM"
            self.current_dept.book_appointment(self.current_patient, selected_doc, time_slot)
        else:
            ConsolePrinter.print_error("Invalid doctor selection.")

    def _generate_token_and_admit(self):
        if not self.current_patient or not self.current_dept:
            ConsolePrinter.print_warning("Please register as a patient first (Option [1]).")
            return
        self.current_dept.admit_patient(self.current_patient)

    def _view_queue_status(self):
        if not self.current_dept:
            self.general_dept.display_queue_status()
            self.emergency_dept.display_queue_status()
            self.vip_dept.display_queue_status()
        else:
            self.current_dept.display_queue_status()

    def _check_wait_time(self):
        if not self.current_patient or not self.current_dept:
            ConsolePrinter.print_warning("Please register/login first.")
            return
        wait = self.current_dept.get_wait_time(self.current_patient.patient_id)
        if wait < 0:
            ConsolePrinter.print_info("You are currently not waiting in the active queue.")
        else:
            ConsolePrinter.print_info(f"Estimated Wait Time for {self.current_patient.name}: {wait} minutes.")

    def _cancel_appointment(self):
        if not self.current_dept:
            ConsolePrinter.print_warning("Please register or select your department.")
            return
        apt_id = input("  >> Enter Appointment ID to Cancel (e.g. APT-GEN-001) : ").strip()
        if apt_id and self.current_patient:
            success = self.current_dept.cancel_appointment(self.current_patient, apt_id)
            if not success:
                ConsolePrinter.print_warning(f"Could not find active appointment with ID: {apt_id}")

    def _reschedule_appointment(self):
        if not self.current_dept:
            ConsolePrinter.print_warning("Please register or select your department.")
            return
        apt_id = input("  >> Enter Appointment ID to Reschedule : ").strip()
        new_time = input("  >> Enter New Time Slot : ").strip()
        if apt_id and new_time and self.current_patient:
            success = self.current_dept.reschedule_appointment(self.current_patient, apt_id, new_time)
            if not success:
                ConsolePrinter.print_warning(f"Could not reschedule appointment: {apt_id}")

    def _view_my_details(self):
        if not self.current_patient:
            ConsolePrinter.print_warning("No active patient logged in. Please register using Option [1].")
            return
        p = self.current_patient
        print(f"\n  {ConsoleColors.BOLD_GREEN}--- CURRENT PATIENT PROFILE ---{ConsoleColors.RESET}")
        print(f"   Patient ID    : {p.patient_id}")
        print(f"   Name          : {p.name}")
        print(f"   Age           : {p.age}")
        print(f"   Contact       : {p.contact}")
        print(f"   Email         : {p.email}")
        print(f"   Department    : {p.department.display_name}")
        print(f"   Emergency?    : {'YES (High Priority)' if p.is_emergency else 'No (Standard)'}")
        if p.token:
            print(f"   Active Token  : #{p.token.token_number:03d} ({p.token.token_id}) - Status: {p.token.status.value}")

    def _get_int_input(self, prompt: str = "  >> Select Option : ") -> int:
        try:
            val = input(f"{ConsoleColors.BOLD_GREEN}{prompt}{ConsoleColors.RESET}").strip()
            return int(val)
        except (ValueError, EOFError):
            return -1


class DoctorDashboard:
    def __init__(self, general_dept: HospitalDepartment, emergency_dept: HospitalDepartment, vip_dept: HospitalDepartment):
        self.general_dept = general_dept
        self.emergency_dept = emergency_dept
        self.vip_dept = vip_dept
        self.current_dept = general_dept

    def show(self):
        self._select_department()
        running = True
        while running:
            self._print_dashboard_menu()
            choice = self._get_int_input()
            if choice == 1:
                self.current_dept.display_schedule()
            elif choice == 2:
                self.current_dept.display_queue_status()
            elif choice == 3:
                served = self.current_dept.serve_next_patient()
                if served:
                    ConsolePrinter.print_success(f"Now Consulting with Patient: {served.name} ({served.patient_id})")
            elif choice == 4:
                self._view_doctors()
            elif choice == 5:
                running = False
            else:
                ConsolePrinter.print_error("Invalid selection.")

    def _select_department(self):
        print(f"\n  {ConsoleColors.BOLD_BLUE}DOCTOR LOGIN — SELECT DEPARTMENT{ConsoleColors.RESET}")
        print(f"   [1] General Department\n   [2] Emergency Department\n   [3] VIP Department")
        ch = self._get_int_input("  >> Select Your Department : ")
        if ch == 2:
            self.current_dept = self.emergency_dept
        elif ch == 3:
            self.current_dept = self.vip_dept
        else:
            self.current_dept = self.general_dept
        ConsolePrinter.print_info(f"Logged in to {self.current_dept.department_name}")

    def _print_dashboard_menu(self):
        print(ConsoleColors.BOLD_BLUE)
        print("  -----------------------------------------------------------------")
        print(f"             DOCTOR DASHBOARD — {self.current_dept.department_name.upper()}")
        print("  -----------------------------------------------------------------")
        print(ConsoleColors.RESET)
        print("   [1]  View Today's Appointments")
        print("   [2]  View Live Department Queue")
        print("   [3]  Serve Next Patient in Queue")
        print("   [4]  View All Doctors in Department")
        print("   [5]  Back to Main Menu")
        print()

    def _view_doctors(self):
        docs = self.current_dept.get_all_doctors()
        print(f"\n  Registered Doctors in {self.current_dept.department_name}:")
        for doc in docs:
            status = "Available" if doc.available else "Busy/Off-duty"
            print(f"   [{doc.doctor_id}] Dr. {doc.name} | {doc.specialization} | Status: {status} | Contact: {doc.contact}")

    def _get_int_input(self, prompt: str = "  >> Select Option : ") -> int:
        try:
            val = input(f"{ConsoleColors.BOLD_BLUE}{prompt}{ConsoleColors.RESET}").strip()
            return int(val)
        except (ValueError, EOFError):
            return -1


class AdminPanel:
    def __init__(self, general_dept: HospitalDepartment, emergency_dept: HospitalDepartment, vip_dept: HospitalDepartment):
        self.general_dept = general_dept
        self.emergency_dept = emergency_dept
        self.vip_dept = vip_dept
        self.emergency_counter = 50

    def show(self):
        running = True
        while running:
            self._print_admin_menu()
            choice = self._get_int_input()
            if choice == 1:
                self._view_all_patients()
            elif choice == 2:
                self._view_all_doctors()
            elif choice == 3:
                self.general_dept.display_queue_status()
                self.emergency_dept.display_queue_status()
                self.vip_dept.display_queue_status()
            elif choice == 4:
                self.general_dept.display_schedule()
                self.emergency_dept.display_schedule()
                self.vip_dept.display_schedule()
            elif choice == 5:
                self._handle_emergency()
            elif choice == 6:
                self._broadcast_alert()
            elif choice == 7:
                self._serve_patient_any_dept()
            elif choice == 8:
                self._view_emergency_patients()
            elif choice == 9:
                self._view_system_summary()
            elif choice == 10:
                running = False
            else:
                ConsolePrinter.print_error("Invalid selection.")

    def _print_admin_menu(self):
        print(ConsoleColors.BOLD_PURPLE)
        print("  =================================================================")
        print("                        ADMIN PANEL                                ")
        print("                   Full System Control                             ")
        print("  =================================================================")
        print(ConsoleColors.RESET)
        print(f"{ConsoleColors.BOLD_WHITE}  -- PATIENT & DOCTOR --")
        print("   [1]   View All Registered Patients")
        print("   [2]   View All Registered Doctors")
        print()
        print("  -- QUEUE & SCHEDULE --")
        print("   [3]   View All Department Queues")
        print("   [4]   View All Appointment Schedules")
        print("   [7]   Serve Next Patient (Any Dept)")
        print()
        print("  -- EMERGENCY --")
        print("   [5]   Handle Emergency Patient Admission")
        print("   [6]   Broadcast System Alert")
        print("   [8]   View Emergency Patients")
        print()
        print("  -- SYSTEM --")
        print("   [9]   View System Summary")
        print(f"   [10]  Back to Main Menu{ConsoleColors.RESET}")
        print()

    def _view_all_patients(self):
        all_pts = self.general_dept.get_all_patients() + self.emergency_dept.get_all_patients() + self.vip_dept.get_all_patients()
        print(f"\n  {ConsoleColors.BOLD_PURPLE}--- ALL REGISTERED PATIENTS ({len(all_pts)}) ---{ConsoleColors.RESET}")
        if not all_pts:
            print("   (No patients registered)")
            return
        for p in all_pts:
            emg_tag = " [EMERGENCY]" if p.is_emergency else ""
            print(f"   [{p.patient_id}] {p.name} (Age: {p.age}) | Dept: {p.department.code}{emg_tag} | Contact: {p.contact}")

    def _view_all_doctors(self):
        all_docs = self.general_dept.get_all_doctors() + self.emergency_dept.get_all_doctors() + self.vip_dept.get_all_doctors()
        print(f"\n  {ConsoleColors.BOLD_PURPLE}--- ALL REGISTERED DOCTORS ({len(all_docs)}) ---{ConsoleColors.RESET}")
        for d in all_docs:
            print(f"   [{d.doctor_id}] Dr. {d.name} | {d.specialization} | Dept: {d.department.display_name}")

    def _handle_emergency(self):
        name = input("  >> Emergency Patient Name : ").strip() or "Emergency Patient"
        age_str = input("  >> Age : ").strip()
        try:
            age = int(age_str)
        except ValueError:
            age = 40
        self.emergency_counter += 1
        pid = DateTimeUtils.generate_patient_id(self.emergency_counter)
        patient = Patient(
            patient_id=pid,
            name=name,
            age=age,
            contact="911",
            email="trauma@hospital.org",
            department=Department.EMERGENCY,
            is_emergency=True,
            registration_time=DateTimeUtils.get_current_timestamp()
        )
        self.emergency_dept.admit_patient(patient)
        docs = self.emergency_dept.get_available_doctors()
        if docs:
            self.emergency_dept.book_appointment(patient, docs[0], "IMMEDIATE")
        self.emergency_dept.broadcast_alert(f"Critical Emergency Code Red: Patient {name} admitted to Trauma Bay!")

    def _broadcast_alert(self):
        msg = input("  >> Enter Alert Message to Broadcast : ").strip()
        if msg:
            self.emergency_dept.broadcast_alert(msg)

    def _serve_patient_any_dept(self):
        print("  Select Department to Serve:")
        print("   [1] General  [2] Emergency  [3] VIP")
        dept_ch = self._get_int_input("  >> Department : ")
        if dept_ch == 2:
            self.emergency_dept.serve_next_patient()
        elif dept_ch == 3:
            self.vip_dept.serve_next_patient()
        else:
            self.general_dept.serve_next_patient()

    def _view_emergency_patients(self):
        pts = [p for p in self.emergency_dept.get_all_patients() if p.is_emergency]
        print(f"\n  {ConsoleColors.BOLD_RED}--- EMERGENCY PATIENT LOG ({len(pts)}) ---{ConsoleColors.RESET}")
        for p in pts:
            print(f"   [{p.patient_id}] {p.name} (Age: {p.age}) | Phone: {p.contact}")

    def _view_system_summary(self):
        all_pts = self.general_dept.get_all_patients() + self.emergency_dept.get_all_patients() + self.vip_dept.get_all_patients()
        all_docs = self.general_dept.get_all_doctors() + self.emergency_dept.get_all_doctors() + self.vip_dept.get_all_doctors()
        gen_q = len(self.general_dept.queue_manager.get_queue_entries())
        emg_q = len(self.emergency_dept.queue_manager.get_queue_entries())
        vip_q = len(self.vip_dept.queue_manager.get_queue_entries())

        print(ConsoleColors.BOLD_CYAN)
        print("  =================================================================")
        print("                     MEDIQUEUE SYSTEM SUMMARY                      ")
        print("  =================================================================")
        print(ConsoleColors.RESET)
        print(f"   Total Patients Registered   : {len(all_pts)}")
        print(f"   Total Doctors on Duty       : {len(all_docs)}")
        print(f"   General Queue Pending       : {gen_q} patient(s)")
        print(f"   Emergency Queue Pending     : {emg_q} critical case(s)")
        print(f"   VIP Lounge Queue Pending    : {vip_q} guest(s)")
        print("  =================================================================")

    def _get_int_input(self, prompt: str = "  >> Select Option : ") -> int:
        try:
            val = input(f"{ConsoleColors.BOLD_PURPLE}{prompt}{ConsoleColors.RESET}").strip()
            return int(val)
        except (ValueError, EOFError):
            return -1


class MainMenu:
    def __init__(self, general_dept: HospitalDepartment, emergency_dept: HospitalDepartment, vip_dept: HospitalDepartment):
        self.general_dept = general_dept
        self.emergency_dept = emergency_dept
        self.vip_dept = vip_dept

    def start(self):
        running = True
        while running:
            ConsolePrinter.print_main_banner()
            print(ConsoleColors.BOLD_WHITE)
            print("  -----------------------------------------------------------------")
            print("                        MAIN MENU                                  ")
            print("  -----------------------------------------------------------------")
            print(ConsoleColors.RESET)
            print(f"{ConsoleColors.BOLD_GREEN}   [1]  Patient Portal{ConsoleColors.RESET}")
            print(f"{ConsoleColors.BOLD_BLUE}   [2]  Doctor Dashboard{ConsoleColors.RESET}")
            print(f"{ConsoleColors.BOLD_PURPLE}   [3]  Admin Panel{ConsoleColors.RESET}")
            print(f"{ConsoleColors.BOLD_RED}   [4]  Exit{ConsoleColors.RESET}")
            print()

            try:
                choice_str = input(f"{ConsoleColors.BOLD_CYAN}  >> Select Option : {ConsoleColors.RESET}").strip()
                choice = int(choice_str)
            except (ValueError, EOFError):
                choice = -1

            if choice == 1:
                PatientPortal(self.general_dept, self.emergency_dept, self.vip_dept).show()
            elif choice == 2:
                DoctorDashboard(self.general_dept, self.emergency_dept, self.vip_dept).show()
            elif choice == 3:
                AdminPanel(self.general_dept, self.emergency_dept, self.vip_dept).show()
            elif choice == 4:
                self._print_exit()
                running = False
            else:
                ConsolePrinter.print_error("Invalid choice. Please select an option between 1 and 4.")

    def _print_exit(self):
        print(ConsoleColors.BOLD_CYAN)
        print("  =================================================================")
        print("         Thank you for using MediQueue System                     ")
        print("         Stay Healthy! Goodbye.                                   ")
        print("  =================================================================")
        print(ConsoleColors.RESET)
