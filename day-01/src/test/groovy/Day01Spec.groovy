import spock.lang.Specification
/**
 * https://adventofcode.com/2025/day/1
 */
class Safe {
    int currentPosition = 50

    void rotate(String instruction) {
        String direction = instruction.substring(0, 1)
        int distance = instruction.substring(1).toInteger()

        if (direction == 'R') {
            currentPosition = (currentPosition + distance) % 100
        } else if (direction == 'L') {
            currentPosition = (currentPosition - distance) % 100
            if (currentPosition < 0) {
                currentPosition += 100
            }
        }
    }
}

class Day01Spec extends Specification {
    def safe = new Safe()

    def "rotate updates position correctly"() {
        given:
        safe.currentPosition = startPos

        when:
        safe.rotate(instruction)

        then:
        safe.currentPosition == endPos

        where:
        startPos | instruction | endPos
        50       | "L68"       | 82
        82       | "L30"       | 52
        52       | "R48"       | 0
        0        | "L5"        | 95
        95       | "R60"       | 55
        55       | "L55"       | 0
        0        | "L1"        | 99
        99       | "L99"       | 0
        0        | "R14"       | 14
        14       | "L82"       | 32
    }

    def "Solve"() {
        given:
        def input = new File("src/test/resources/input.txt").readLines()
        int zeroCount = 0

        when:
        input.each { instruction ->
            safe.rotate(instruction)
            if (safe.currentPosition == 0) {
                zeroCount++
            }
        }

        then:
        println "Password: $zeroCount"
        zeroCount >= 0 // minimal assertion
    }
}
