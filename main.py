"""
MediQueue — Smart Hospital Queue & Appointment Management System
Main Entry Point (Interactive User Input Mode)

Course : ISWE404L — Design Pattern
Team   : Sandhiya O H (23MIS0114) | Deepak Kumar S (23MIS0191)
"""
import sys
import os

# Ensure package is on sys.path
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

from mediqueue.models import Department, Doctor
from mediqueue.patterns import (
    HospitalDepartment,
    GeneralHospitalFactory,
    EmergencyHospitalFactory,
    VIPHospitalFactory
)
from mediqueue.ui import MainMenu
from mediqueue.utils import DateTimeUtils


def initialize_default_doctors(general_dept, emergency_dept, vip_dept):
    doc1 = Doctor(
        doctor_id=DateTimeUtils.generate_doctor_id(1),
        name="Ramesh Kumar",
        specialization="General Physician",
        department=Department.GENERAL,
        available=True,
        contact="9876543001"
    )
    doc2 = Doctor(
        doctor_id=DateTimeUtils.generate_doctor_id(2),
        name="Priya Menon",
        specialization="Emergency Specialist",
        department=Department.EMERGENCY,
        available=True,
        contact="9876543002"
    )
    doc3 = Doctor(
        doctor_id=DateTimeUtils.generate_doctor_id(3),
        name="Arjun Nair",
        specialization="Senior Consultant",
        department=Department.VIP,
        available=True,
        contact="9876543003"
    )

    general_dept.register_doctor(doc1)
    emergency_dept.register_doctor(doc2)
    vip_dept.register_doctor(doc3)


def main():
    # Initialize department instances using the Abstract Factory Pattern
    general_dept = HospitalDepartment(GeneralHospitalFactory())
    emergency_dept = HospitalDepartment(EmergencyHospitalFactory())
    vip_dept = HospitalDepartment(VIPHospitalFactory())

    # Pre-populate doctors
    initialize_default_doctors(general_dept, emergency_dept, vip_dept)

    # Launch interactive user-input driven main menu
    menu = MainMenu(general_dept, emergency_dept, vip_dept)
    menu.start()


if __name__ == "__main__":
    main()
