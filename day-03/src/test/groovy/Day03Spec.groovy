import spock.lang.Specification

/**
 * https://adventofcode.com/2025/day/3
 */
class Day03Spec extends Specification {
    def "Solve"() {
        setup:
        def inputFile = new File("src/test/resources/input.txt")
        def input = inputFile.readLines()

        when:
        def totalJoltage = input.sum { line ->
            calculateMaxJoltage(line)
        }

        then:
        println "Total output joltage: $totalJoltage"
        totalJoltage != 0
    }

    def "calculateMaxJoltage correctly identifies max joltage"() {
        expect:
        calculateMaxJoltage(bank) == expectedMax

        where:
        bank                  | expectedMax
        "987654321111111"     | 98
        "811111111111119"     | 89
        "234234234234278"     | 78
        "818181911112111"     | 92
        "12345"               | 45 // Example from my own head: 4 then 5 is max possible (wait, 5 is at index 4, 4 is at index 3. 45 is valid. 54 is not) incorrect logic check?
                                   // "12345": pairs: 12, 13, 14, 15, 23, 24, 25, 34, 35, 45. Max is 45. Correct.
    }

    int calculateMaxJoltage(String bank) {
        int maxJoltage = 0
        for (int i = 0; i < bank.length() - 1; i++) {
            for (int j = i + 1; j < bank.length(); j++) {
                int firstDigit = Character.getNumericValue(bank.charAt(i))
                int secondDigit = Character.getNumericValue(bank.charAt(j))
                int joltage = firstDigit * 10 + secondDigit
                if (joltage > maxJoltage) {
                    maxJoltage = joltage
                }
            }
        }
        return maxJoltage
    }
}
