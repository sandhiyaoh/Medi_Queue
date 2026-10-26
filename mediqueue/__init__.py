"""
MediQueue Package
"""
from mediqueue.models import Department, Doctor, Patient, Token, Appointment, QueueEntry
from mediqueue.patterns import (
    HospitalDepartment,
    GeneralHospitalFactory,
    EmergencyHospitalFactory,
    VIPHospitalFactory
)
