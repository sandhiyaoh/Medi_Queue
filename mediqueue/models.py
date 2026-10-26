"""
Core domain models for MediQueue
"""
from dataclasses import dataclass
from enum import Enum
from typing import Optional


class Department(Enum):
    GENERAL = ("GEN", "General Department", 15)
    EMERGENCY = ("EMG", "Emergency Department", 0)
    VIP = ("VIP", "VIP Department", 5)

    def __init__(self, code: str, display_name: str, avg_wait_minutes: int):
        self.code = code
        self.display_name = display_name
        self.avg_wait_minutes = avg_wait_minutes

    @classmethod
    def from_string(cls, name_or_code: str):
        cleaned = name_or_code.strip().upper()
        for dept in cls:
            if dept.name == cleaned or dept.code == cleaned or dept.display_name.upper() == cleaned:
                return dept
        return cls.GENERAL


class AppointmentStatus(Enum):
    SCHEDULED = "SCHEDULED"
    COMPLETED = "COMPLETED"
    CANCELLED = "CANCELLED"
    RESCHEDULED = "RESCHEDULED"


class TokenStatus(Enum):
    WAITING = "WAITING"
    CALLED = "CALLED"
    COMPLETED = "COMPLETED"
    CANCELLED = "CANCELLED"


@dataclass
class Doctor:
    doctor_id: str
    name: str
    specialization: str
    department: Department
    available: bool = True
    contact: str = ""

    def __str__(self):
        return f"Dr. {self.name} ({self.specialization}) [{self.department.display_name}]"


@dataclass
class Token:
    token_id: str
    patient_id: str
    patient_name: str
    department: Department
    token_number: int
    issued_time: str
    status: TokenStatus = TokenStatus.WAITING

    def __str__(self):
        return f"Token #{self.token_number:03d} ({self.token_id}) - {self.status.value}"


@dataclass
class Patient:
    patient_id: str
    name: str
    age: int
    contact: str
    email: str
    department: Department
    is_emergency: bool = False
    registration_time: str = ""
    token: Optional[Token] = None

    def __str__(self):
        tag = "[EMERGENCY]" if self.is_emergency else ""
        return f"[{self.patient_id}] {self.name} (Age: {self.age}) {tag} - {self.department.display_name}"


@dataclass
class QueueEntry:
    PRIORITY_EMERGENCY = 1
    PRIORITY_VIP = 2
    PRIORITY_GENERAL = 3

    patient: Patient
    token: Token
    priority: int
    position: int
    wait_time: int
    timestamp: str

    def __lt__(self, other: "QueueEntry"):
        # Lower priority number gets served first (1 = emergency, 2 = VIP, 3 = general)
        if self.priority != other.priority:
            return self.priority < other.priority
        return self.token.token_number < other.token.token_number


@dataclass
class Appointment:
    appointment_id: str
    patient: Patient
    doctor: Doctor
    time_slot: str
    department: Department
    status: AppointmentStatus = AppointmentStatus.SCHEDULED
    created_at: str = ""

    def __str__(self):
        return f"[{self.appointment_id}] Patient: {self.patient.name} with Dr. {self.doctor.name} at {self.time_slot} ({self.status.value})"
