import spock.lang.Specification

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
    }

    def "crack the password"() {
        // TODO: 
        expect:
        true
    }
}
