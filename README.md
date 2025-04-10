
# Raspberry Pi Elevator Controller (BlueStaq Solutions)

An embedded Java-powered elevator simulation platform, running on a Raspberry Pi 3B+ with a 4x4 membrane keypad, 16x2 LCD character display, and systemd-bootable logic.

This project was designed, built, tested, and deployed to demonstrate end-to-end embedded control—from software architecture to physical integration and real-time service execution.

---

## Features

- Java-powered elevator logic
- 4x4 membrane keypad for physical input
- 16x2 LCD character display
- Built with Pi4J GPIO integration
- Fully systemd daemonized for boot-time autolaunch
- Laser-engraved hardware instruction card
- 3D-printed enclosures for polish, branding, and protection

---

## Design Iterations & Key Decisions

### Initial Planning & Language Choice
- Chose Java over Python based on challenge criteria
- Learned IntelliJ IDEA during project execution
- Final builds and service control done directly via CLI on Raspberry Pi

### Console-Based Architecture First
- Started as a text-driven Java app
- Implemented elevator scan-traversal logic (HDD-style)
- Added door logic, emergency logic, and multi-floor targeting

### Major Pivot: Time-Driven Architecture
- Moved from prompt-driven to real-time loop
- Rebuilt system to function autonomously using physical I/O

### Platform Shift: Arduino to Raspberry Pi
- Dropped Arduino due to Java compatibility limits
- Restarted development on Raspberry Pi 3B+

### Hardware Integration
- 16x2 LCD for status display
- 4x4 Keypad for control input (0–9, A–D, *, #)
- Laser-engraved instruction card
- Custom 3D-printed housing for keypad and LCD

### Power & Mobility Decisions
- Targeted 5V @ 2.7A supply (2.4A power bank was insufficient)
- Opted for wall plug + HDMI + USB for stable demo setup

### Code Structure & Extensibility
- Modular architecture with abstract elevator base
- Input abstraction for GPIO or terminal fallback
- Future support planned for GUI-based control

---

## Troubleshooting Philosophy

"A problem well-defined is a problem half-solved."

- Documented issues clearly before making changes
- Limited scope of each test cycle to avoid cascading bugs
- Walked away and returned with clarity when progress stalled

---

## Integration Challenges Overcome

- Pi4J installation on Raspberry Pi (manual fix of broken GitHub links)
- GPIO runtime access and permission elevation (group membership, pigpiod daemon)
- Slow compile/test cycles (3–5 mins per change)
- Complex boot-time daemonization (solved via systemd service creation)
- LCD readout formatting, ghosting, and refresh timing
- Source path confusion, case sensitivity, and Pi4J plugin classpath errors
- Lack of local jumper cables or GPIO parts; overnight sourcing required

---

## Directory Structure

bluestaq-elevator/
├── src/
│   └── com/geraldmaloney/elevator/...
├── out/                          # Compiled class files
├── bluestack-elevator.service    # systemd startup config
├── demo/                         # Images, videos, logs
├── README.md

---

## Raspberry Pi Commands

### Compile and Run
export CP="out:/opt/pi4j/lib/pi4j-core.jar:/opt/pi4j/lib/pi4j-plugin-pigpio.jar:/opt/pi4j/lib/pi4j-plugin-raspberrypi.jar:/opt/pi4j/lib/pi4j-plugin-linuxfs.jar:/opt/pi4j/lib/pi4j-plugin-pigpiod.jar:/opt/pi4j/lib/pi4j-library-pigpio.jar:/opt/pi4j/lib/slf4j-api.jar:/opt/pi4j/lib/slf4j-simple.jar"
javac -cp "$CP" -d out $(find src -name "*.java")
java -cp "$CP" com.geraldmaloney.elevator.Main

### Systemd Service Control
sudo systemctl start bluestack-elevator.service
sudo systemctl stop bluestack-elevator.service
sudo systemctl status bluestack-elevator.service
sudo journalctl -u bluestack-elevator.service -f

---

## Improvements I'd Make (Next Iterations)

1. Scale Up to a GUI-Based Application
2. Improve Hardware (OLED, capacitive touch)
3. Overhaul Console Output
4. Enhance LCD Output (scrolling, alerts)
5. Add Multi-Floor Interrupt Threading
6. Improve Power Integration
7. Refactor Core Logic for Reuse
8. Add Diagnostic Mode at Boot

---

## Demo Artifacts

Demo media and documentation are located in the /media or /demo folders, including:
- LCD boot photos
- Hardware demo video
- Laser-engraved instruction card
- Wiring layout image
- 3D printed case images
- Terminal logs showing system startup

---

## Final Note

This project was built under real-world constraints, from scratch, and tested on actual hardware. It includes physical integration, power management, embedded control, and documentation readiness. It showcases not just technical skill, but design discipline and resilience under pressure.
