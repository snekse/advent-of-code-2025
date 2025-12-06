import spock.lang.Specification

/**
 * https://adventofcode.com/2025/day/3
 */
class Day03Spec extends Specification {
    def "Solve Part 1"() {
        setup:
        def inputFile = new File("src/test/resources/input.txt")
        def input = inputFile.readLines()

        when:
        def totalJoltage = input.sum { line ->
            calculateMaxJoltage(line, 2)
        }

        then:
        println "Part 1 Total output joltage: $totalJoltage"
        totalJoltage != 0
    }

    def "Solve Part 2"() {
        setup:
        def inputFile = new File("src/test/resources/input.txt")
        def input = inputFile.readLines()

        when:
        def totalJoltage = input.sum { line ->
            calculateMaxJoltage(line, 12)
        }

        then:
        println "Part 2 New total output joltage: $totalJoltage"
        totalJoltage == 167523425665348
    }

    def "calculateMaxJoltage correctly identifies max joltage for length 2"() {
        expect:
        calculateMaxJoltage(bank, 2) == expectedMax

        where:
        bank                  | expectedMax
        "987654321111111"     | 98
        "811111111111119"     | 89
        "234234234234278"     | 78
        "818181911112111"     | 92
        "12345"               | 45
    }

    def "calculateMaxJoltage correctly identifies max joltage for length 12"() {
        expect:
        calculateMaxJoltage(bank, 12) == expectedMax

        where:
        bank                  | expectedMax
        "987654321111111"     | 987654321111L
        "811111111111119"     | 811111111119L
        "234234234234278"     | 434234234278L
        "818181911112111"     | 888911112111L
    }

    long calculateMaxJoltage(String bank, int length) {
        StringBuilder maxJoltageStr = new StringBuilder()
        int currentSearchIndex = 0

        for (int i = 0; i < length; i++) {
            // Find the largest digit in the valid remaining range
            // We need to pick (length - i) more digits (including this one)
            // So we must stop searching at: bank.length() - (length - i)
            // Example: length 2, i=0. Need 2 total. Remaining 2. Stop at len - 2.
            // i=1. Need 1. Remaining 1. Stop at len - 1 (last char).
            
            char maxDigit = '/' // '0' - 1
            int foundAtIndex = -1
            int searchEnd = bank.length() - (length - i)

            for (int j = currentSearchIndex; j <= searchEnd; j++) {
                char currentDigit = bank.charAt(j)
                if (currentDigit > maxDigit) {
                    maxDigit = currentDigit
                    foundAtIndex = j
                }
                if (maxDigit == '9') break // Optimization: can't get better than 9
            }

            maxJoltageStr.append(maxDigit)
            currentSearchIndex = foundAtIndex + 1
        }
        
        return Long.parseLong(maxJoltageStr.toString())
    }
}
