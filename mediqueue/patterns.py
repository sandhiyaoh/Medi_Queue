"""
Abstract Factory Pattern implementation for MediQueue
"""
from abc import ABC, abstractmethod
import heapq
from typing import List, Optional

from mediqueue.models import (
    Department, Doctor, Patient, Token, TokenStatus,
    QueueEntry, Appointment, AppointmentStatus
)
from mediqueue.utils import ConsoleColors, ConsolePrinter, DateTimeUtils


# ============================================================
# ABSTRACT PRODUCT INTERFACES
# ============================================================

class QueueManager(ABC):
    @abstractmethod
    def add_patient_to_queue(self, patient: Patient) -> None:
        pass

    @abstractmethod
    def serve_next_patient(self) -> Optional[Patient]:
        pass

    @abstractmethod
    def display_queue(self) -> None:
        pass

    @abstractmethod
    def manage_queue(self) -> None:
        pass

    @abstractmethod
    def is_queue_empty(self) -> bool:
        pass

    @abstractmethod
    def get_queue_entries(self) -> List[QueueEntry]:
        pass

    @abstractmethod
    def get_estimated_wait_time(self, patient_id: str) -> int:
        pass


class AppointmentScheduler(ABC):
    @abstractmethod
    def schedule(self, patient: Patient, doctor: Doctor, time_slot: str) -> Optional[Appointment]:
        pass

    @abstractmethod
    def cancel_appointment(self, appointment_id: str) -> bool:
        pass

    @abstractmethod
    def reschedule_appointment(self, appointment_id: str, new_time: str) -> bool:
        pass

    @abstractmethod
    def display_schedule(self) -> None:
        pass

    @abstractmethod
    def get_todays_appointments(self) -> List[Appointment]:
        pass


class NotificationService(ABC):
    @abstractmethod
    def notify_patient(self, patient: Patient, message: str) -> None:
        pass

    @abstractmethod
    def notify_doctor(self, doctor: Doctor, message: str) -> None:
        pass

    @abstractmethod
    def notify_appointment_booked(self, patient: Patient, time_slot: str) -> None:
        pass

    @abstractmethod
    def notify_appointment_cancelled(self, patient: Patient, appointment_id: str) -> None:
        pass

    @abstractmethod
    def notify_patient_turn(self, patient: Patient, token_number: int) -> None:
        pass

    @abstractmethod
    def broadcast_emergency_alert(self, message: str) -> None:
        pass

    @abstractmethod
    def get_notification_channel(self) -> str:
        pass


# ============================================================
# CONCRETE PRODUCTS — GENERAL DEPARTMENT
# ============================================================

class GeneralQueue(QueueManager):
    def __init__(self):
        self.queue: List[QueueEntry] = []
        self.token_counter = 1

    def add_patient_to_queue(self, patient: Patient) -> None:
        token = Token(
            token_id=DateTimeUtils.generate_token_id("GEN", self.token_counter),
            patient_id=patient.patient_id,
            patient_name=patient.name,
            department=Department.GENERAL,
            token_number=self.token_counter,
            issued_time=DateTimeUtils.get_current_timestamp(),
            status=TokenStatus.WAITING
        )
        patient.token = token
        position = len(self.queue) + 1
        wait_time = DateTimeUtils.calculate_wait_time("GEN", position)

        entry = QueueEntry(
            patient=patient,
            token=token,
            priority=QueueEntry.PRIORITY_GENERAL,
            position=position,
            wait_time=wait_time,
            timestamp=DateTimeUtils.get_current_timestamp()
        )
        self.queue.append(entry)
        self.token_counter += 1

        ConsolePrinter.print_success(
            f"{ConsoleColors.BOLD_GREEN}[GENERAL] Patient Queued -> {patient.name} | "
            f"Token #{token.token_number:03d} | Est. Wait: {wait_time} mins{ConsoleColors.RESET}"
        )

    def serve_next_patient(self) -> Optional[Patient]:
        if not self.queue:
            return None
        entry = self.queue.pop(0)
        entry.token.status = TokenStatus.COMPLETED
        self._recalculate_positions()
        return entry.patient

    def _recalculate_positions(self):
        for idx, entry in enumerate(self.queue):
            entry.position = idx + 1
            entry.wait_time = DateTimeUtils.calculate_wait_time("GEN", entry.position)

    def display_queue(self) -> None:
        print(f"\n  {ConsoleColors.BOLD_GREEN}--- GENERAL DEPARTMENT QUEUE (FIFO) ---{ConsoleColors.RESET}")
        if not self.queue:
            print(f"  {ConsoleColors.WHITE}(Queue is currently empty){ConsoleColors.RESET}")
            return
        for entry in self.queue:
            print(
                f"   [Pos #{entry.position}] Token #{entry.token.token_number:03d} "
                f"| Patient: {entry.patient.name} ({entry.patient.patient_id}) "
                f"| Est. Wait: {entry.wait_time} mins"
            )

    def manage_queue(self) -> None:
        pass

    def is_queue_empty(self) -> bool:
        return len(self.queue) == 0

    def get_queue_entries(self) -> List[QueueEntry]:
        return list(self.queue)

    def get_estimated_wait_time(self, patient_id: str) -> int:
        for entry in self.queue:
            if entry.patient.patient_id == patient_id:
                return entry.wait_time
        return -1


class GeneralScheduler(AppointmentScheduler):
    def __init__(self):
        self.appointments: List[Appointment] = []
        self.apt_counter = 1

    def schedule(self, patient: Patient, doctor: Doctor, time_slot: str) -> Optional[Appointment]:
        apt = Appointment(
            appointment_id=DateTimeUtils.generate_appointment_id("GEN", self.apt_counter),
            patient=patient,
            doctor=doctor,
            time_slot=time_slot,
            department=Department.GENERAL,
            status=AppointmentStatus.SCHEDULED,
            created_at=DateTimeUtils.get_current_timestamp()
        )
        self.appointments.append(apt)
        self.apt_counter += 1
        ConsolePrinter.print_success(
            f"[GENERAL] Appointment Booked: {patient.name} with Dr. {doctor.name} at {time_slot} ({apt.appointment_id})"
        )
        return apt

    def cancel_appointment(self, appointment_id: str) -> bool:
        for apt in self.appointments:
            if apt.appointment_id == appointment_id and apt.status != AppointmentStatus.CANCELLED:
                apt.status = AppointmentStatus.CANCELLED
                ConsolePrinter.print_info(f"[GENERAL] Appointment {appointment_id} marked CANCELLED")
                return True
        return False

    def reschedule_appointment(self, appointment_id: str, new_time: str) -> bool:
        for apt in self.appointments:
            if apt.appointment_id == appointment_id and apt.status != AppointmentStatus.CANCELLED:
                apt.time_slot = new_time
                apt.status = AppointmentStatus.RESCHEDULED
                ConsolePrinter.print_success(f"[GENERAL] Appointment {appointment_id} rescheduled to {new_time}")
                return True
        return False

    def display_schedule(self) -> None:
        print(f"\n  {ConsoleColors.BOLD_GREEN}--- GENERAL APPOINTMENTS SCHEDULE ---{ConsoleColors.RESET}")
        if not self.appointments:
            print(f"  {ConsoleColors.WHITE}(No appointments booked){ConsoleColors.RESET}")
            return
        for apt in self.appointments:
            print(f"   [{apt.appointment_id}] {apt.time_slot} | Patient: {apt.patient.name} | Dr. {apt.doctor.name} | Status: {apt.status.value}")

    def get_todays_appointments(self) -> List[Appointment]:
        return [a for a in self.appointments if a.status != AppointmentStatus.CANCELLED]


class SMSNotification(NotificationService):
    def notify_patient(self, patient: Patient, message: str) -> None:
        print(f"  {ConsoleColors.CYAN}[SMS -> {patient.contact}]{ConsoleColors.RESET} Dear {patient.name}: {message}")

    def notify_doctor(self, doctor: Doctor, message: str) -> None:
        print(f"  {ConsoleColors.CYAN}[SMS -> Dr. {doctor.name}]{ConsoleColors.RESET} {message}")

    def notify_appointment_booked(self, patient: Patient, time_slot: str) -> None:
        self.notify_patient(patient, f"Your appointment is confirmed for {time_slot}.")

    def notify_appointment_cancelled(self, patient: Patient, appointment_id: str) -> None:
        self.notify_patient(patient, f"Your appointment {appointment_id} has been cancelled.")

    def notify_patient_turn(self, patient: Patient, token_number: int) -> None:
        self.notify_patient(patient, f"Now Serving! Please proceed to the consultation room (Token #{token_number:03d}).")

    def broadcast_emergency_alert(self, message: str) -> None:
        print(f"  {ConsoleColors.BOLD_YELLOW}[SMS BROADCAST]{ConsoleColors.RESET} {message}")

    def get_notification_channel(self) -> str:
        return "SMS"


# ============================================================
# CONCRETE PRODUCTS — EMERGENCY DEPARTMENT
# ============================================================

class EmergencyQueue(QueueManager):
    def __init__(self):
        self.heap: List[QueueEntry] = []
        self.token_counter = 1

    def add_patient_to_queue(self, patient: Patient) -> None:
        token = Token(
            token_id=DateTimeUtils.generate_token_id("EMG", self.token_counter),
            patient_id=patient.patient_id,
            patient_name=patient.name,
            department=Department.EMERGENCY,
            token_number=self.token_counter,
            issued_time=DateTimeUtils.get_current_timestamp(),
            status=TokenStatus.WAITING
        )
        patient.token = token
        entry = QueueEntry(
            patient=patient,
            token=token,
            priority=QueueEntry.PRIORITY_EMERGENCY,
            position=1,
            wait_time=0,
            timestamp=DateTimeUtils.get_current_timestamp()
        )
        heapq.heappush(self.heap, entry)
        self.token_counter += 1

        ConsolePrinter.print_error(
            f"{ConsoleColors.BOLD_RED}[EMERGENCY] Patient Admitted -> {patient.name} | "
            f"Token #{token.token_number:03d} | IMMEDIATE TRIAGE (Wait: 0 mins){ConsoleColors.RESET}"
        )

    def serve_next_patient(self) -> Optional[Patient]:
        if not self.heap:
            return None
        entry = heapq.heappop(self.heap)
        entry.token.status = TokenStatus.COMPLETED
        return entry.patient

    def display_queue(self) -> None:
        print(f"\n  {ConsoleColors.BOLD_RED}--- EMERGENCY DEPARTMENT QUEUE (TRIAGE PRIORITY) ---{ConsoleColors.RESET}")
        if not self.heap:
            print(f"  {ConsoleColors.WHITE}(Emergency queue is clear){ConsoleColors.RESET}")
            return
        sorted_entries = sorted(self.heap)
        for idx, entry in enumerate(sorted_entries):
            print(
                f"   [PRIORITY #{idx + 1}] Token #{entry.token.token_number:03d} "
                f"| Patient: {entry.patient.name} ({entry.patient.patient_id}) "
                f"| Status: CRITICAL IMMEDIATE"
            )

    def manage_queue(self) -> None:
        pass

    def is_queue_empty(self) -> bool:
        return len(self.heap) == 0

    def get_queue_entries(self) -> List[QueueEntry]:
        return sorted(self.heap)

    def get_estimated_wait_time(self, patient_id: str) -> int:
        for entry in self.heap:
            if entry.patient.patient_id == patient_id:
                return 0
        return -1


class EmergencyScheduler(AppointmentScheduler):
    def __init__(self):
        self.appointments: List[Appointment] = []
        self.apt_counter = 1

    def schedule(self, patient: Patient, doctor: Doctor, time_slot: str) -> Optional[Appointment]:
        slot = "IMMEDIATE" if not time_slot else time_slot
        apt = Appointment(
            appointment_id=DateTimeUtils.generate_appointment_id("EMG", self.apt_counter),
            patient=patient,
            doctor=doctor,
            time_slot=slot,
            department=Department.EMERGENCY,
            status=AppointmentStatus.SCHEDULED,
            created_at=DateTimeUtils.get_current_timestamp()
        )
        self.appointments.append(apt)
        self.apt_counter += 1
        ConsolePrinter.print_error(
            f"[EMERGENCY] Immediate Appointment Allocated: {patient.name} with Dr. {doctor.name} ({apt.appointment_id})"
        )
        return apt

    def cancel_appointment(self, appointment_id: str) -> bool:
        for apt in self.appointments:
            if apt.appointment_id == appointment_id:
                apt.status = AppointmentStatus.CANCELLED
                return True
        return False

    def reschedule_appointment(self, appointment_id: str, new_time: str) -> bool:
        for apt in self.appointments:
            if apt.appointment_id == appointment_id:
                apt.time_slot = new_time
                apt.status = AppointmentStatus.RESCHEDULED
                return True
        return False

    def display_schedule(self) -> None:
        print(f"\n  {ConsoleColors.BOLD_RED}--- EMERGENCY APPOINTMENTS SCHEDULE ---{ConsoleColors.RESET}")
        if not self.appointments:
            print(f"  {ConsoleColors.WHITE}(No emergency appointments){ConsoleColors.RESET}")
            return
        for apt in self.appointments:
            print(f"   [{apt.appointment_id}] {apt.time_slot} | Patient: {apt.patient.name} | Dr. {apt.doctor.name} | Status: {apt.status.value}")

    def get_todays_appointments(self) -> List[Appointment]:
        return [a for a in self.appointments if a.status != AppointmentStatus.CANCELLED]


class EmergencyAlertNotification(NotificationService):
    def notify_patient(self, patient: Patient, message: str) -> None:
        print(f"  {ConsoleColors.BOLD_RED}[EMERGENCY DISPATCH -> {patient.contact}]{ConsoleColors.RESET} {message}")

    def notify_doctor(self, doctor: Doctor, message: str) -> None:
        print(f"  {ConsoleColors.BOLD_RED}[EMERGENCY CALL -> Dr. {doctor.name}]{ConsoleColors.RESET} {message}")

    def notify_appointment_booked(self, patient: Patient, time_slot: str) -> None:
        self.notify_patient(patient, f"EMERGENCY ADMISSION booked for slot: {time_slot}. Trauma staff alerted.")

    def notify_appointment_cancelled(self, patient: Patient, appointment_id: str) -> None:
        self.notify_patient(patient, f"Emergency record {appointment_id} updated/closed.")

    def notify_patient_turn(self, patient: Patient, token_number: int) -> None:
        print(f"  {ConsoleColors.BOLD_RED}! [EMERGENCY STAT CALL]{ConsoleColors.RESET} Patient {patient.name} (Token #{token_number:03d}) to Trauma Unit NOW!")

    def broadcast_emergency_alert(self, message: str) -> None:
        print(f"  {ConsoleColors.BOLD_RED}!! [EMERGENCY WIDE BROADCAST] !! {message}{ConsoleColors.RESET}")

    def get_notification_channel(self) -> str:
        return "SMS + EMERGENCY ALERT"


# ============================================================
# CONCRETE PRODUCTS — VIP DEPARTMENT
# ============================================================

class PremiumQueue(QueueManager):
    def __init__(self):
        self.queue: List[QueueEntry] = []
        self.token_counter = 1

    def add_patient_to_queue(self, patient: Patient) -> None:
        token = Token(
            token_id=DateTimeUtils.generate_token_id("VIP", self.token_counter),
            patient_id=patient.patient_id,
            patient_name=patient.name,
            department=Department.VIP,
            token_number=self.token_counter,
            issued_time=DateTimeUtils.get_current_timestamp(),
            status=TokenStatus.WAITING
        )
        patient.token = token
        position = len(self.queue) + 1
        wait_time = DateTimeUtils.calculate_wait_time("VIP", position)

        entry = QueueEntry(
            patient=patient,
            token=token,
            priority=QueueEntry.PRIORITY_VIP,
            position=position,
            wait_time=wait_time,
            timestamp=DateTimeUtils.get_current_timestamp()
        )
        self.queue.append(entry)
        self.token_counter += 1

        ConsolePrinter.print_info(
            f"{ConsoleColors.BOLD_PURPLE}[VIP] Premium Patient Queued -> {patient.name} | "
            f"Token #{token.token_number:03d} | Expedited Wait: {wait_time} mins{ConsoleColors.RESET}"
        )

    def serve_next_patient(self) -> Optional[Patient]:
        if not self.queue:
            return None
        entry = self.queue.pop(0)
        entry.token.status = TokenStatus.COMPLETED
        for idx, item in enumerate(self.queue):
            item.position = idx + 1
            item.wait_time = DateTimeUtils.calculate_wait_time("VIP", item.position)
        return entry.patient

    def display_queue(self) -> None:
        print(f"\n  {ConsoleColors.BOLD_PURPLE}--- VIP LOUNGE QUEUE (EXPEDITED) ---{ConsoleColors.RESET}")
        if not self.queue:
            print(f"  {ConsoleColors.WHITE}(VIP lounge queue is clear){ConsoleColors.RESET}")
            return
        for entry in self.queue:
            print(
                f"   [VIP #{entry.position}] Token #{entry.token.token_number:03d} "
                f"| Patient: {entry.patient.name} ({entry.patient.patient_id}) "
                f"| Lounge Wait: {entry.wait_time} mins"
            )

    def manage_queue(self) -> None:
        pass

    def is_queue_empty(self) -> bool:
        return len(self.queue) == 0

    def get_queue_entries(self) -> List[QueueEntry]:
        return list(self.queue)

    def get_estimated_wait_time(self, patient_id: str) -> int:
        for entry in self.queue:
            if entry.patient.patient_id == patient_id:
                return entry.wait_time
        return -1


class PriorityScheduler(AppointmentScheduler):
    def __init__(self):
        self.appointments: List[Appointment] = []
        self.apt_counter = 1

    def schedule(self, patient: Patient, doctor: Doctor, time_slot: str) -> Optional[Appointment]:
        apt = Appointment(
            appointment_id=DateTimeUtils.generate_appointment_id("VIP", self.apt_counter),
            patient=patient,
            doctor=doctor,
            time_slot=time_slot,
            department=Department.VIP,
            status=AppointmentStatus.SCHEDULED,
            created_at=DateTimeUtils.get_current_timestamp()
        )
        self.appointments.append(apt)
        self.apt_counter += 1
        ConsolePrinter.print_info(
            f"[VIP] Concierge Appointment Booked: {patient.name} with Senior Consultant Dr. {doctor.name} at {time_slot} ({apt.appointment_id})"
        )
        return apt

    def cancel_appointment(self, appointment_id: str) -> bool:
        for apt in self.appointments:
            if apt.appointment_id == appointment_id and apt.status != AppointmentStatus.CANCELLED:
                apt.status = AppointmentStatus.CANCELLED
                ConsolePrinter.print_info(f"[VIP] Priority Appointment {appointment_id} cancelled.")
                return True
        return False

    def reschedule_appointment(self, appointment_id: str, new_time: str) -> bool:
        for apt in self.appointments:
            if apt.appointment_id == appointment_id and apt.status != AppointmentStatus.CANCELLED:
                apt.time_slot = new_time
                apt.status = AppointmentStatus.RESCHEDULED
                ConsolePrinter.print_success(f"[VIP] Concierge Rescheduled {appointment_id} to {new_time}")
                return True
        return False

    def display_schedule(self) -> None:
        print(f"\n  {ConsoleColors.BOLD_PURPLE}--- VIP EXECUTIVE APPOINTMENTS SCHEDULE ---{ConsoleColors.RESET}")
        if not self.appointments:
            print(f"  {ConsoleColors.WHITE}(No VIP appointments scheduled){ConsoleColors.RESET}")
            return
        for apt in self.appointments:
            print(f"   [{apt.appointment_id}] {apt.time_slot} | VIP Guest: {apt.patient.name} | Consultant: Dr. {apt.doctor.name} | Status: {apt.status.value}")

    def get_todays_appointments(self) -> List[Appointment]:
        return [a for a in self.appointments if a.status != AppointmentStatus.CANCELLED]


class PremiumNotification(NotificationService):
    def notify_patient(self, patient: Patient, message: str) -> None:
        print(f"  {ConsoleColors.BOLD_PURPLE}[VIP CONCIERGE -> {patient.email} & {patient.contact}]{ConsoleColors.RESET} Valued VIP Guest {patient.name}: {message}")

    def notify_doctor(self, doctor: Doctor, message: str) -> None:
        print(f"  {ConsoleColors.BOLD_PURPLE}[VIP CONSULTANT NOTIFICATION -> Dr. {doctor.name}]{ConsoleColors.RESET} {message}")

    def notify_appointment_booked(self, patient: Patient, time_slot: str) -> None:
        self.notify_patient(patient, f"Your priority suite appointment is confirmed for {time_slot}. Concierge desk notified.")

    def notify_appointment_cancelled(self, patient: Patient, appointment_id: str) -> None:
        self.notify_patient(patient, f"VIP appointment {appointment_id} has been modified/cancelled.")

    def notify_patient_turn(self, patient: Patient, token_number: int) -> None:
        print(f"  {ConsoleColors.BOLD_PURPLE}* [VIP CONCIERGE CALL]{ConsoleColors.RESET} Guest {patient.name} (Token #{token_number:03d}), your executive suite is ready.")

    def broadcast_emergency_alert(self, message: str) -> None:
        print(f"  {ConsoleColors.BOLD_PURPLE}[VIP SUITE ALERT]{ConsoleColors.RESET} {message}")

    def get_notification_channel(self) -> str:
        return "EMAIL + SMS + CONCIERGE"


# ============================================================
# ABSTRACT FACTORY INTERFACE
# ============================================================

class HospitalFactory(ABC):
    @abstractmethod
    def create_queue_manager(self) -> QueueManager:
        pass

    @abstractmethod
    def create_appointment_scheduler(self) -> AppointmentScheduler:
        pass

    @abstractmethod
    def create_notification_service(self) -> NotificationService:
        pass

    @abstractmethod
    def get_department(self) -> Department:
        pass

    @abstractmethod
    def display_factory_info(self) -> None:
        pass


# ============================================================
# CONCRETE FACTORIES
# ============================================================

class GeneralHospitalFactory(HospitalFactory):
    def create_queue_manager(self) -> QueueManager:
        return GeneralQueue()

    def create_appointment_scheduler(self) -> AppointmentScheduler:
        return GeneralScheduler()

    def create_notification_service(self) -> NotificationService:
        return SMSNotification()

    def get_department(self) -> Department:
        return Department.GENERAL

    def display_factory_info(self) -> None:
        print(f"  {ConsoleColors.BOLD_GREEN}[FACTORY] GeneralHospitalFactory -> GeneralQueue + GeneralScheduler + SMSNotification{ConsoleColors.RESET}")


class EmergencyHospitalFactory(HospitalFactory):
    def create_queue_manager(self) -> QueueManager:
        return EmergencyQueue()

    def create_appointment_scheduler(self) -> AppointmentScheduler:
        return EmergencyScheduler()

    def create_notification_service(self) -> NotificationService:
        return EmergencyAlertNotification()

    def get_department(self) -> Department:
        return Department.EMERGENCY

    def display_factory_info(self) -> None:
        print(f"  {ConsoleColors.BOLD_RED}[FACTORY] EmergencyHospitalFactory -> EmergencyQueue + EmergencyScheduler + EmergencyAlertNotification{ConsoleColors.RESET}")


class VIPHospitalFactory(HospitalFactory):
    def create_queue_manager(self) -> QueueManager:
        return PremiumQueue()

    def create_appointment_scheduler(self) -> AppointmentScheduler:
        return PriorityScheduler()

    def create_notification_service(self) -> NotificationService:
        return PremiumNotification()

    def get_department(self) -> Department:
        return Department.VIP

    def display_factory_info(self) -> None:
        print(f"  {ConsoleColors.BOLD_PURPLE}[FACTORY] VIPHospitalFactory -> PremiumQueue + PriorityScheduler + PremiumNotification{ConsoleColors.RESET}")


# ============================================================
# CLIENT CLASS — HospitalDepartment
# ============================================================

class HospitalDepartment:
    """
    Client class that works exclusively with abstract interfaces.
    Zero coupling to concrete product classes!
    """
    def __init__(self, factory: HospitalFactory):
        self.factory = factory
        self.queue_manager: QueueManager = factory.create_queue_manager()
        self.scheduler: AppointmentScheduler = factory.create_appointment_scheduler()
        self.notification_service: NotificationService = factory.create_notification_service()
        self.department: Department = factory.get_department()
        self.department_name: str = self.department.display_name

        self.doctors: List[Doctor] = []
        self.patients: List[Patient] = []

        ConsolePrinter.print_info(
            f"Department Initialized -> {self.department_name} | Channel : {self.notification_service.get_notification_channel()}"
        )

    def register_doctor(self, doctor: Doctor) -> None:
        self.doctors.append(doctor)

    def get_available_doctors(self) -> List[Doctor]:
        return [d for d in self.doctors if d.available]

    def get_all_doctors(self) -> List[Doctor]:
        return self.doctors

    def register_patient(self, patient: Patient) -> None:
        if patient not in self.patients:
            self.patients.append(patient)

    def get_all_patients(self) -> List[Patient]:
        return self.patients

    def admit_patient(self, patient: Patient) -> None:
        self.register_patient(patient)
        self.queue_manager.add_patient_to_queue(patient)
        self.notification_service.notify_patient(
            patient, f"You have been added to the {self.department_name} queue."
        )

    def book_appointment(self, patient: Patient, doctor: Doctor, time_slot: str) -> Optional[Appointment]:
        self.register_patient(patient)
        apt = self.scheduler.schedule(patient, doctor, time_slot)
        if apt:
            self.notification_service.notify_appointment_booked(patient, time_slot)
            self.notification_service.notify_doctor(
                doctor, f"New appointment booked with {patient.name} at {time_slot}"
            )
        return apt

    def cancel_appointment(self, patient: Patient, appointment_id: str) -> bool:
        cancelled = self.scheduler.cancel_appointment(appointment_id)
        if cancelled:
            self.notification_service.notify_appointment_cancelled(patient, appointment_id)
        return cancelled

    def reschedule_appointment(self, patient: Patient, appointment_id: str, new_time: str) -> bool:
        rescheduled = self.scheduler.reschedule_appointment(appointment_id, new_time)
        if rescheduled:
            self.notification_service.notify_appointment_booked(patient, new_time)
        return rescheduled

    def serve_next_patient(self) -> Optional[Patient]:
        if self.queue_manager.is_queue_empty():
            ConsolePrinter.print_warning(f"Queue in {self.department_name} is empty. No patients to serve.")
            return None

        entries = self.queue_manager.get_queue_entries()
        token_num = entries[0].token.token_number if entries else 0

        patient = self.queue_manager.serve_next_patient()
        if patient:
            self.notification_service.notify_patient_turn(patient, token_num)
        return patient

    def display_queue_status(self) -> None:
        self.queue_manager.display_queue()

    def display_schedule(self) -> None:
        self.scheduler.display_schedule()

    def get_wait_time(self, patient_id: str) -> int:
        return self.queue_manager.get_estimated_wait_time(patient_id)

    def broadcast_alert(self, message: str) -> None:
        self.notification_service.broadcast_emergency_alert(message)
