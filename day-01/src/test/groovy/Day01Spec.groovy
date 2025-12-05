import spock.lang.Specification

class Safe {
    int currentPosition = 50

    int rotate(String instruction) {
        String direction = instruction.substring(0, 1)
        int distance = instruction.substring(1).toInteger()
        int zeroHits = 0

        distance.times {
            if (direction == 'R') {
                currentPosition = (currentPosition + 1) % 100
            } else if (direction == 'L') {
                currentPosition = (currentPosition - 1)
                if (currentPosition < 0) {
                    currentPosition += 100
                }
            }
            if (currentPosition == 0) {
                zeroHits++
            }
        }
        return zeroHits
    }
}

/**
 * https://adventofcode.com/2025/day/1
 */
class Day01Spec extends Specification {
    def safe = new Safe()

    def "rotate updates position correctly"() {
        given:
        safe.currentPosition = startPos

        when:
        int hits = safe.rotate(instruction)

        then:
        safe.currentPosition == endPos
        hits == expectedHits

        where:
        startPos | instruction | endPos | expectedHits
        50       | "L68"       | 82     | 1
        82       | "L30"       | 52     | 0
        52       | "R48"       | 0      | 1
        0        | "L5"        | 95     | 0
        95       | "R60"       | 55     | 1
        55       | "L55"       | 0      | 1
        0        | "L1"        | 99     | 0
        99       | "L99"       | 0      | 1
        0        | "R14"       | 14     | 0
        14       | "L82"       | 32     | 1
    }

    def "Solve Part 1"() {
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

    def "Solve Part 2"() {
        given:
        def input = new File("src/test/resources/input.txt").readLines()
        int totalZeroHits = 0

        when:
        input.each { instruction ->
            totalZeroHits += safe.rotate(instruction)
        }

        then:
        println "Password Part 2: $totalZeroHits"
        totalZeroHits >= 0
    }
}
