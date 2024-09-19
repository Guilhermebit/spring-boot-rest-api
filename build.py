import subprocess
import platform
import sys

DOCKER_COMPOSE_DOWN = "docker-compose down"
DOCKER_COMPOSE_UP = "docker-compose up --build -d"
DOCKER_PRUNE = "docker container prune -f"
DOCKER_PS_ALL = "docker ps -aq"
DOCKER_STOP = "docker container stop"
MVN_WINDOWS = "mvnw.cmd clean package"
MVN_UNIX = "./mvnw clean package"


def os_platform():
    return platform.system()


def run_command(command):
    # Run a shell command and show the output.
    try:
        result = subprocess.run(command, shell=True, check=True, text=True, capture_output=True)
        print(result.stdout.strip())
    except subprocess.CalledProcessError as er:
        print(f"Error: {er.stderr.strip()}")
        sys.exit(1)


def build_project():
    # Compiles the project using Maven Wrapper (or Maven).
    print("Building project...")

    os_name = os_platform()
    print(f"System detected: {os_name}")

    command = MVN_UNIX
    if os_name == "Windows":
        command = MVN_WINDOWS

    run_command(command)


def remove_remaining_containers():
    # Stop and remove Docker containers from the current project, and restart the containers.
    print("Stopping and removing all containers...")
    run_command(DOCKER_COMPOSE_DOWN)

    # Check if there is a container running
    remaining_containers = subprocess.check_output(DOCKER_PS_ALL, shell=True, text=True).strip().split('\n')
    remaining_containers = [container for container in remaining_containers if container]  # Remove empty entries

    if not remaining_containers:
        print("No remaining containers.")
        return

    print(f"There are still {len(remaining_containers)} containers created: {remaining_containers}")
    for container_id in remaining_containers:
        print(f"Stopping container {container_id}")
        run_command(f"{DOCKER_STOP} {container_id}")

    # Remove stopped containers.
    run_command(DOCKER_PRUNE)


def docker_compose_up():
    run_command(DOCKER_COMPOSE_UP)
    print("Running containers!")


if __name__ == "__main__":
    try:
        print("Pipeline started!")
        build_project()
        remove_remaining_containers()
        docker_compose_up()
        print("Pipeline finished!")
        print("Application has been updated successfully!")
        sys.exit(0)
    except RuntimeError as e:
        print(f"An error occurred: {e}")
        sys.exit(1)
