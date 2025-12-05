import spock.lang.Specification
/**
 * https://adventofcode.com/2025/day/1
 */
class Safe {
    int currentValue = 50
    int zeroCount = 0

    int turnDial(String instruction) {
        def direction = instruction[0]
        def amount = instruction.substring(1) as int

        if (direction == 'L') {
            currentValue -= amount
        } else {
            currentValue += amount
        }

        currentValue %= 100
        if (currentValue < 0) currentValue += 100

        if (currentValue == 0) {
            zeroCount++
        }

        return currentValue
    }
}

class Day01Spec extends Specification {
    def safe = new Safe()

    def "Test turnDial"() {
        expect:
        safe.turnDial(instruction) == expectedValue
        safe.zeroCount == zeroCount

        where:
        instruction || expectedValue || zeroCount
        'L0'        || 50            || 0
        'R0'        || 50            || 0
        'L1'        || 49            || 0
        'R1'        || 51            || 0
        'L50'       || 0             || 1
        'R50'       || 0             || 1
        'L51'       || 99            || 0
        'R51'       || 1             || 0
        'L100'      || 50            || 1
        'R100'      || 50            || 1
        'L150'      || 0             || 2
        'R150'      || 0             || 2
        'L200'      || 50            || 2
        'R200'      || 50            || 2
    }

    def "real password test"() {
        when:
        safe.turnDial('L50') // get to 0
        then:
        safe.zeroCount == 1
        when:
        safe.turnDial('R1')
        then:
        safe.zeroCount == 1
        when:
        safe.turnDial('L1')
        then:
        safe.zeroCount == 2
        when:
        safe.turnDial('L1')
        then:
        safe.zeroCount == 2
        when:
        safe.turnDial('R1')
        then:
        safe.zeroCount == 3

    }

    def "crack the password"() {
        given:
        def resource = getClass().getResource('/input.txt')
        assert resource != null : "Could not find input.txt"
        def file = new File(resource.toURI())

        when:
        file.eachLine { line ->
            if (line.trim()) {
                def val = line.trim()
                def newValue = safe.turnDial(val)
                println "Turn dial: $val -> $newValue"
            }
        }
        println "Zero Count: ${safe.zeroCount}"

        then:
        safe.currentValue != null
    }
}
