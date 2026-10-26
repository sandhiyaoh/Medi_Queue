"""
Console colors and utilities for MediQueue
"""
from datetime import datetime

class ConsoleColors:
    RESET = "\033[0m"
    BOLD = "\033[1m"
    
    RED = "\033[31m"
    GREEN = "\033[32m"
    YELLOW = "\033[33m"
    BLUE = "\033[34m"
    PURPLE = "\033[35m"
    CYAN = "\033[36m"
    WHITE = "\033[37m"
    
    BOLD_RED = "\033[1;31m"
    BOLD_GREEN = "\033[1;32m"
    BOLD_YELLOW = "\033[1;33m"
    BOLD_BLUE = "\033[1;34m"
    BOLD_PURPLE = "\033[1;35m"
    BOLD_CYAN = "\033[1;36m"
    BOLD_WHITE = "\033[1;37m"


class ConsolePrinter:
    @staticmethod
    def print_main_banner():
        print(ConsoleColors.BOLD_CYAN)
        print("  =================================================================")
        print("         MediQueue : Smart Hospital Management System             ")
        print("         Design Pattern  : Abstract Factory                       ")
        print("         Team            : Sandhiya O H | Deepak Kumar S          ")
        print("  =================================================================")
        print(ConsoleColors.RESET)

    @staticmethod
    def print_section_header(title: str):
        print(ConsoleColors.BOLD_PURPLE)
        print("  " + "=" * 65)
        print(f"   {title.center(61)}")
        print("  " + "=" * 65)
        print(ConsoleColors.RESET)

    @staticmethod
    def print_sub_header(title: str):
        print(ConsoleColors.BOLD_CYAN)
        print("  " + "-" * 65)
        print(f"   {title}")
        print("  " + "-" * 65)
        print(ConsoleColors.RESET)

    @staticmethod
    def print_info(message: str):
        print(f"  {ConsoleColors.BOLD_CYAN}[INFO]{ConsoleColors.RESET}  {message}")

    @staticmethod
    def print_success(message: str):
        print(f"  {ConsoleColors.BOLD_GREEN}[OK]{ConsoleColors.RESET}    {message}")

    @staticmethod
    def print_warning(message: str):
        print(f"  {ConsoleColors.BOLD_YELLOW}[WARN]{ConsoleColors.RESET}  {message}")

    @staticmethod
    def print_error(message: str):
        print(f"  {ConsoleColors.BOLD_RED}[ERR]{ConsoleColors.RESET}   {message}")

    @staticmethod
    def print_blank_line():
        print()


class DateTimeUtils:
    @staticmethod
    def get_current_timestamp() -> str:
        return datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    @staticmethod
    def generate_token_id(dept_code: str, count: int) -> str:
        return f"TKN-{dept_code}-{count:03d}"

    @staticmethod
    def generate_patient_id(count: int) -> str:
        return f"PAT-{count:03d}"

    @staticmethod
    def generate_doctor_id(count: int) -> str:
        return f"DOC-{count:03d}"

    @staticmethod
    def generate_appointment_id(dept_code: str, count: int) -> str:
        return f"APT-{dept_code}-{count:03d}"

    @staticmethod
    def calculate_wait_time(dept_code: str, position: int) -> int:
        if dept_code == "EMG":
            return 0
        elif dept_code == "VIP":
            return max(0, (position - 1) * 5)
        else:
            return max(0, (position - 1) * 15)
