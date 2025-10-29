# Proximity Radar – Homework Skeleton

Minimal Maven project for IntelliJ IDEA with JUnit 5.

## How to open in IntelliJ

1. **File → New → Project from Existing Sources...**
2. Choose this folder (`proximity-radar-maven`).
3. Select **Maven** when prompted. Finish.
4. Make sure **JDK 17** (or higher) is selected: *File → Project Structure → Project SDK*.
5. Tests live in `src/test/java`, code in `src/main/java`.

## Run tests

- From IntelliJ: right‑click the `test` package → **Run 'All Tests'**.
- From terminal:
  ```bash
  mvn -q -DskipTests=false test
  ```

## Package structure

- Package: `edu.tedu.radar`
- Add your classes (`Radar`, `Target`, `Detection`, `ThreatLevel`) under `src/main/java/edu/tedu/radar/`.
- Add your tests under `src/test/java/edu/tedu/radar/`.

## Notes

- JUnit 5 is already configured (including parameterized tests).
- Add more CSV rows/assertions to reach the required 40 assertions.
