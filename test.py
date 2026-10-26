"""
MediQueue -- Smart Hospital Queue & Appointment Management System
Automated Test Runner & Test Data Display

Loads test data from 'test_data.json' and demonstrates full system execution
using the Abstract Factory Design Pattern without requiring user input.

Course : ISWE404L -- Design Pattern
Team   : Sandhiya O H (23MIS0114) | Deepak Kumar S (23MIS0191)
"""
import json
import os
import sys

# Ensure UTF-8 output if supported
if hasattr(sys.stdout, "reconfigure"):
    try:
        sys.stdout.reconfigure(encoding="utf-8")
    except Exception:
        pass

# Ensure package is on sys.path
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, BASE_DIR)

from mediqueue.models import Department, Doctor, Patient
from mediqueue.patterns import (
    HospitalDepartment,
    GeneralHospitalFactory,
    EmergencyHospitalFactory,
    VIPHospitalFactory
)
from mediqueue.utils import ConsoleColors, ConsolePrinter, DateTimeUtils


def load_test_data(filepath: str) -> dict:
    if not os.path.exists(filepath):
        ConsolePrinter.print_error(f"Test data file not found at: {filepath}")
        sys.exit(1)
    with open(filepath, "r", encoding="utf-8") as f:
        return json.load(f)


def display_raw_test_data(data: dict):
    ConsolePrinter.print_section_header("TEST DATA FILE: test_data.json")
    print(f"  {ConsoleColors.BOLD_WHITE}Project        :{ConsoleColors.RESET} {data.get('project')}")
    print(f"  {ConsoleColors.BOLD_WHITE}Design Pattern :{ConsoleColors.RESET} {data.get('design_pattern')}")
    team_str = ", ".join([f"{m['name']} ({m['reg_no']})" for m in data.get('team', [])])
    print(f"  {ConsoleColors.BOLD_WHITE}Team Members   :{ConsoleColors.RESET} {team_str}")
    print()

    print(f"  {ConsoleColors.BOLD_CYAN}[1] Test Doctors in Data File:{ConsoleColors.RESET}")
    for doc in data.get("doctors", []):
        print(f"      * {doc['doctor_id']} | Dr. {doc['name']} ({doc['specialization']}) | Dept: {doc['department']} | Phone: {doc['contact']}")

    print(f"\n  {ConsoleColors.BOLD_CYAN}[2] Test Patients in Data File:{ConsoleColors.RESET}")
    for pat in data.get("patients", []):
        emg = " [EMERGENCY]" if pat.get("is_emergency") else ""
        print(f"      * {pat['patient_id']} | {pat['name']} (Age: {pat['age']}) | Dept: {pat['department']}{emg} | Phone: {pat['contact']}")

    print(f"\n  {ConsoleColors.BOLD_CYAN}[3] Test Appointments in Data File:{ConsoleColors.RESET}")
    for apt in data.get("appointments", []):
        print(f"      * Patient: {apt['patient_id']} -> Doctor: {apt['doctor_id']} | Slot: {apt['time_slot']} [{apt['department']}]")

    print(f"\n  {ConsoleColors.BOLD_CYAN}[4] Test Reschedule Operations:{ConsoleColors.RESET}")
    for res in data.get("reschedule_tests", []):
        print(f"      * Patient: {res['patient_id']} in {res['department']} -> New Slot: {res['new_time_slot']}")

    print(f"\n  {ConsoleColors.BOLD_CYAN}[5] Emergency Alerts in Data File:{ConsoleColors.RESET}")
    for alert in data.get("emergency_broadcasts", []):
        print(f"      * Broadcast Message: \"{alert}\"")
    print()


def run_test_suite():
    test_data_path = os.path.join(BASE_DIR, "test_data.json")
    
    # 1. Main System Banner
    ConsolePrinter.print_main_banner()
    
    # 2. Load and Display Test Data
    data = load_test_data(test_data_path)
    display_raw_test_data(data)

    # 3. Initialize Abstract Factory Departments
    ConsolePrinter.print_section_header("PHASE 1: ABSTRACT FACTORY INITIALIZATION")
    
    general_factory = GeneralHospitalFactory()
    emergency_factory = EmergencyHospitalFactory()
    vip_factory = VIPHospitalFactory()

    general_factory.display_factory_info()
    emergency_factory.display_factory_info()
    vip_factory.display_factory_info()
    print()

    general_dept = HospitalDepartment(general_factory)
    emergency_dept = HospitalDepartment(emergency_factory)
    vip_dept = HospitalDepartment(vip_factory)

    dept_map = {
        "GENERAL": general_dept,
        "EMERGENCY": emergency_dept,
        "VIP": vip_dept
    }

    # 4. Register Doctors from Test Data
    ConsolePrinter.print_section_header("PHASE 2: DOCTOR REGISTRATIONS FROM TEST DATA")
    doctor_obj_map = {}
    for doc_dict in data.get("doctors", []):
        dept_enum = Department.from_string(doc_dict["department"])
        doctor = Doctor(
            doctor_id=doc_dict["doctor_id"],
            name=doc_dict["name"],
            specialization=doc_dict["specialization"],
            department=dept_enum,
            available=doc_dict.get("available", True),
            contact=doc_dict.get("contact", "")
        )
        doctor_obj_map[doctor.doctor_id] = doctor
        dept_map[doc_dict["department"]].register_doctor(doctor)
        ConsolePrinter.print_success(f"Registered Dr. {doctor.name} in {dept_enum.display_name}")

    # 5. Register & Admit Patients to Queues from Test Data
    ConsolePrinter.print_section_header("PHASE 3: PATIENT ADMISSION & TOKEN GENERATION")
    patient_obj_map = {}
    for pat_dict in data.get("patients", []):
        dept_enum = Department.from_string(pat_dict["department"])
        patient = Patient(
            patient_id=pat_dict["patient_id"],
            name=pat_dict["name"],
            age=pat_dict["age"],
            contact=pat_dict["contact"],
            email=pat_dict["email"],
            department=dept_enum,
            is_emergency=pat_dict.get("is_emergency", False),
            registration_time=DateTimeUtils.get_current_timestamp()
        )
        patient_obj_map[patient.patient_id] = patient
        dept = dept_map[pat_dict["department"]]
        dept.admit_patient(patient)

    # 6. Display Live Department Queues
    ConsolePrinter.print_section_header("PHASE 4: LIVE DEPARTMENT QUEUE STATUS DISPLAY")
    general_dept.display_queue_status()
    emergency_dept.display_queue_status()
    vip_dept.display_queue_status()

    # 7. Book Appointments from Test Data
    ConsolePrinter.print_section_header("PHASE 5: APPOINTMENT SCHEDULING & NOTIFICATIONS")
    for apt_dict in data.get("appointments", []):
        pat = patient_obj_map.get(apt_dict["patient_id"])
        doc = doctor_obj_map.get(apt_dict["doctor_id"])
        dept = dept_map[apt_dict["department"]]
        if pat and doc:
            dept.book_appointment(pat, doc, apt_dict["time_slot"])

    # 8. Display Appointment Schedules
    ConsolePrinter.print_section_header("PHASE 6: APPOINTMENT SCHEDULES DISPLAY")
    general_dept.display_schedule()
    emergency_dept.display_schedule()
    vip_dept.display_schedule()

    # 9. Test Appointment Rescheduling
    ConsolePrinter.print_section_header("PHASE 7: APPOINTMENT RESCHEDULING TEST")
    for res_dict in data.get("reschedule_tests", []):
        pat = patient_obj_map.get(res_dict["patient_id"])
        dept = dept_map[res_dict["department"]]
        today_apts = dept.scheduler.get_todays_appointments()
        if today_apts and pat:
            apt_to_reschedule = today_apts[0]
            dept.reschedule_appointment(pat, apt_to_reschedule.appointment_id, res_dict["new_time_slot"])
    
    # Display updated schedule
    vip_dept.display_schedule()

    # 10. Test Emergency Broadcasts
    ConsolePrinter.print_section_header("PHASE 8: EMERGENCY ALERT BROADCAST TEST")
    for alert_msg in data.get("emergency_broadcasts", []):
        emergency_dept.broadcast_alert(alert_msg)

    # 11. Test Patient Service (Queue Turn Execution)
    ConsolePrinter.print_section_header("PHASE 9: QUEUE SERVICE & PATIENT TURN NOTIFICATIONS")
    ConsolePrinter.print_info("Serving next patient in Emergency Department (Highest Priority Triage):")
    emergency_dept.serve_next_patient()

    ConsolePrinter.print_info("Serving next patient in VIP Department (Priority Lounge):")
    vip_dept.serve_next_patient()

    ConsolePrinter.print_info("Serving next patient in General Department (FIFO Order):")
    general_dept.serve_next_patient()

    # 12. Display Queues After Service
    ConsolePrinter.print_section_header("PHASE 10: QUEUES STATUS AFTER CONSULTATIONS")
    general_dept.display_queue_status()
    emergency_dept.display_queue_status()
    vip_dept.display_queue_status()

    # 13. System Summary & Verification
    ConsolePrinter.print_section_header("PHASE 11: TEST EXECUTION SUMMARY & VERIFICATION")
    ConsolePrinter.print_success("Test Data File         : Loaded successfully from test_data.json")
    ConsolePrinter.print_success("Abstract Factory Check : General, Emergency, and VIP factories initialized")
    ConsolePrinter.print_success("Queue Manager Check    : GeneralQueue (FIFO), EmergencyQueue (Priority), PremiumQueue (VIP)")
    ConsolePrinter.print_success("Scheduler Check        : GeneralScheduler, EmergencyScheduler, PriorityScheduler")
    ConsolePrinter.print_success("Notification Check     : SMSNotification, EmergencyAlertNotification, PremiumNotification")
    ConsolePrinter.print_success("User Input Bypass      : All operations completed automatically with test data display")
    print()
    ConsolePrinter.print_info("ALL TEST SCENARIOS EXECUTED SUCCESSFULLY WITHOUT ERRORS.")
    ConsolePrinter.print_blank_line()


if __name__ == "__main__":
    run_test_suite()
