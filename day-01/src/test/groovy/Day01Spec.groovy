import spock.lang.Specification

import spock.lang.Ignore

/**
 * https://adventofcode.com/2025/day/1
 */
class Safe {
    int currentValue = 50
    int passedZeroCount = 0
    int landedZeroCount = 0

    int getZeroCount() {
        return passedZeroCount + landedZeroCount
    }

    int turnDial(String instruction) {
        def direction = instruction[0]
        def amount = instruction.substring(1) as int
        assert amount >= 0 : "Amount must be >=0"
        def startValue = currentValue

        if (amount == 0) {
            return currentValue
        }

        if (amount > 100) {
            int rotations = (amount / 100) as int
            passedZeroCount += rotations
            amount -= (rotations * 100)
        }

        if (direction == 'L') {
            currentValue -= amount
        } else {
            currentValue += amount
        }

        if(currentValue==100 || currentValue == 0) {
            landedZeroCount++
            currentValue = 0
        } else if (currentValue > 100) {
            passedZeroCount++ // passed zero
            currentValue %= 100
        } else if (currentValue < 0) {
            if (startValue != 0) {
                passedZeroCount++ // passed zero
            }
            currentValue += 100
        }

        // println "Final Current Value: $currentValue"
        // println "Final Zero Counts: $passedZeroCount && $landedZeroCount"
        return currentValue
    }
}

class Day01Spec extends Specification {
    def safe = new Safe()

    def "Test turnDial from Start"() {
        safe.currentValue = start

        expect:
        safe.turnDial(instruction) == expectedValue
        safe.zeroCount == zeroCount

        where:
        start | instruction || expectedValue || zeroCount
        0     | 'L0'        || 0             || 0
        0     | 'R1'        || 1             || 0  
        0     | 'L1'        || 99            || 0
        99    | 'R1'        || 0             || 1
        1     | 'L1'        || 0             || 1
        70    | 'R568'      || 38            || 6
        62    | 'R233'      || 95            || 2
    }

    def "Test turnDial :: #instruction"() {
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
        'L51'       || 99            || 1
        'R51'       || 1             || 1
        'L100'      || 50            || 1
        'R100'      || 50            || 1
        'L150'      || 0             || 2
        'R150'      || 0             || 2
        'L200'      || 50            || 2
        'R200'      || 50            || 2
        'L250'      || 0             || 3
        'R250'      || 0             || 3
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
        safe.currentValue = 50
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
        println "Landed Zero Count: ${safe.landedZeroCount}"
        println "Passed Zero Count: ${safe.passedZeroCount}"

        then:
        safe.currentValue != null
        safe.landedZeroCount == 1048
        safe.zeroCount == 6498
    }
}
