import spock.lang.Specification

/**
 * https://adventofcode.com/2025/day/3
 */
class Day03Spec extends Specification {

    /**
     * NOTE: If I would have know the inputs were 100 char each, I would have done this differently
     * Since we know the inputs are only digits 0-9, we coudld search until we find.
     */
    int getHighestJoltage(String input) {
        def charList = input.toList()
        def posCharList = charList.indexed()
            .collect { idx, v -> [idx, v] }

        // find the max digit
        def highestDigitStr = charList.max()
        def highestPosList = posCharList
            .findAll { it[1] == highestDigitStr }

        // early exit if we have 2 or more highest digits
        // e.g. 1234909 => 99
        if (highestPosList.size() >= 2) {
            return (highestDigitStr * 2) as int
        }

        // find the next highest digit and their positions
        def nextHighestDigitStr = charList.findAll { it != highestDigitStr }.max()
        def nextHighestPosList = posCharList
            .findAll { it[1] == nextHighestDigitStr }


        // check if highest digit is at the end, 
        // if so we just need to find next highest digit - done for speed
        if (highestPosList.last()[0] == charList.size() - 1) {
            return "$nextHighestDigitStr$highestDigitStr" as int
        }

        // Put both posLists into a single list and sort it by index 0, 
        // then take index 1 and join them
        return (highestPosList + nextHighestPosList)
            .sort { it[0] } // and get the first 2 elements
            .take(2)
            .collect { it[1] }
            .join() as int
    
    }

    /**
    * This may not be any faster.  For `numbersFound` => [[5, 2, 31], [4, 0, 1]]
    * this means we walked 100 chars for 9..6 and found nothing
    */
    int getHighestJoltageFast(String input) {
        def numbersFound = []

        // exit once we find 2 highest digits
        (9..0).forEach { it -> 
            if (numbersFound.size() >= 2) {
                return
            }
            def num = it as String
            def idx = input.indexOf(num)
            if (idx == -1) {
                return
            }
            def idx2 = input.indexOf(num, idx + 1)
            def result = [num, idx, idx2]
            numbersFound << result
        }

        println numbersFound

        // if first digit has 2 indexes, return that
        def highestDigitDetails = numbersFound[0]
        if (highestDigitDetails[2] != -1) {
            return (highestDigitDetails[0] * 2) as int
        } 

        def secondHighestDigitDetails = numbersFound[1]
        // is highest digit the last character of input
        if (highestDigitDetails[0] == input[-1]) {
            return "${secondHighestDigitDetails[0]}${highestDigitDetails[0]}" as int
        }

        return (highestDigitDetails[0] + '' + secondHighestDigitDetails[0]) as int

        // Put both posLists into a single list and sort it by 1st index, 
        // then take index number and join them
        return (highestDigitDetails + secondHighestDigitDetails)
            .sort { it[1] } // and get the first 2 elements
            .take(2)
            .collect { it[0] }
            .join() as int
    }

    def "simple highest Joltage test"() {
        expect:
        getHighestJoltage("181819") == 89
    }

    def " Get highest Joltage"() {
        expect:
        getHighestJoltageFast(input) == expected
        getHighestJoltage(input) == expected

        where: 
        input               || expected 
        "987654321111111"   || 98
        "123456789"         || 89
        "811111111111119"   || 89
        "234234234234278"   || 78
        "818181911112111"   || 92
        "818181911812111"   || 98
    }



    def "Solve"() {
        // read input file and process each line    
        def result = new File("src/test/resources/input.txt")
            .text
            .split("\n").collect { line ->
                def result = getHighestJoltageFast(line.trim())
                println result
                return result
            }.sum(0)
        println "result: $result"

        expect:
        result == 16882
    }
}
