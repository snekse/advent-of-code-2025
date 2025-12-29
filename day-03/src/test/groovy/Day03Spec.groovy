import spock.lang.Specification
import spock.lang.Ignore

class Banks {
    int maxSize = 12
    // ArrayList is preferred over LinkedList here despite frequent insertions at index 0.
    // For small sizes (12), CPU cache locality makes array shifting faster than pointer chasing.
    List<String> stack = new ArrayList<>(maxSize)

    void push(String value) {
        
        if (size() < maxSize) {
            //println "size() < maxSize: ${size() < maxSize}"
            stack.push(value)
            return
        }

        if (value < stack.first() || isMaxValue()) {
            // println "value < stack.head() || isMaxValue: ${value < stack.head() || isMaxValue}"
            return
        }

        trim()

        // println "Pushing $value onto stack"
        stack.add(0, value)
    }

    private void trim() {
        // Find the first index where the current digit is less than the next digit
        // This indicates a position where removing the current digit yields a larger number
        def indexToRemove = (0..<maxSize - 1).find { i -> stack[i] < stack[i + 1] }

        if (indexToRemove != null) {
            stack.remove((int) indexToRemove)
        } else {
            // If no such digit exists, remove the last one to make space
            stack.removeLast()
        }
    }

    boolean isMaxValue() {
        isFull() && stack.every { it == '9' }
    }
    
    boolean isFull() {
        size() == maxSize
    }

    int size() {
        stack.size()
    }
}

class BanksSpec extends Specification {
    def banks = new Banks()

    def "Banks push first element"() {
        when:
        banks.push("1")

        then:
        banks.stack == ["1"]
    }

    def "Banks trim works as expected" () {
        def input = '19283746556382'.reverse()
        input.each { banks.push(it) }
        
        when:
        def result = banks.stack.join() as long

        then:
        result == 983746556382
    }
}

/**
 * https://adventofcode.com/2025/day/3
 */
class Day03Spec extends Specification {


    /**
    * This may not be any faster.  For `numbersFound` => [[5, 2, 31], [4, 0, 1]]
    * this means we walked 100 chars for 9..6 and found nothing
    * e.g. 818181911112111 => 92
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
            def idx3 = input.lastIndexOf(num)
            def result = [num, idx, idx2, idx3]
            numbersFound << result
        }

        println numbersFound

        // if first digit has 2 indexes, return that
        def highestDigitDetails = numbersFound[0]
        if (highestDigitDetails[2] != -1) {
            return (highestDigitDetails[0] * 2) as int
        } 

        def secondHighestDigitDetails = numbersFound[1]
        
        if (highestDigitDetails[0] == input[-1]) {
            // highest digit the last character of input
            return "${secondHighestDigitDetails[0]}${highestDigitDetails[0]}" as int
        } else if (secondHighestDigitDetails[3] > highestDigitDetails[1]) {
            return "${highestDigitDetails[0]}${secondHighestDigitDetails[0]}" as int
        } else if (secondHighestDigitDetails[3] < highestDigitDetails[1]) {
            // need to find the 3rd highest substringing from last position of highest digit
            def thirdDigit = input.substring(highestDigitDetails[3] + 1).toList().max()
            return "${highestDigitDetails[0]}${thirdDigit}" as int
        }
        

        return "${secondHighestDigitDetails[0]}${highestDigitDetails[0]}" as int
        
    }

    def "simple highest Joltage test"() {
        expect:
        getHighestJoltageFast("818181911812111") == 98
    }

    def "Get highest Joltage"() {
        expect:
        getHighestJoltageFast(input) == expected

        where: 
        input               || expected 
        "987654321111111"   || 98
        "123456789"         || 89
        "811111111111119"   || 89
        "234234234234278"   || 78
        "818181911112111"   || 92
        "818181911812111"   || 98
        "789"               || 89
        "7890"              || 90
        "1238181911"        || 91
        "892"               || 92
    }

    @Ignore // this asserts a value for input from part 1
    def "Solve"() {
        // read input file and process each line    
        def sum = new File("src/test/resources/input.txt")
            .text
            .split("\n").collect { line ->
                def result = getHighestJoltageFast(line.trim())
                println result
                return result
            }.sum(0)
        println "sum: $sum"

        expect:
        sum == 16842
    }

    def "Solve Part 2 with Banks"() {
        // read input file and process each line    
        def sum = 0L
        new File("src/test/resources/input.txt").eachLine { line ->
            def banks = new Banks()
            // Process reversed line
            line.trim().reverse().each { banks.push(it) }
            def result = banks.stack.join() as long
            sum += result
        }
        println "sum: $sum"

        expect:
        sum == 167523425665348
    }
}
