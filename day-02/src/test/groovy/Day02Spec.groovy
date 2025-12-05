import spock.lang.Specification

/**
 * https://adventofcode.com/2025/day/2
 */
class Day02Spec extends Specification {
    boolean isInvalid(Long id) {
        String s = id.toString()
        if (s.length() % 2 != 0) {
            return false
        }
        String firstHalf = s.substring(0, s.length() / 2 as int)
        String secondHalf = s.substring(s.length() / 2 as int)
        return firstHalf == secondHalf
    }

    def "Check invalid logic"() {
        expect:
        isInvalid(id) == expected

        where:
        id     | expected
        11     | true
        22     | true
        99     | true
        55     | true
        6464   | true
        123123 | true
        1010   | true
        222222 | true
        12     | false
        123    | false
        101    | false
    }

    def "Solve"() {
        setup:
        File inputFile = new File("src/test/resources/input.txt")
        String input = inputFile.text.trim()
        
        List<String> rangeStrings = input.split(",") as List
        List<Long> invalidIds = []

        rangeStrings.each { String rangeStr ->
            def parts = rangeStr.split("-")
            Long start = parts[0] as Long
            Long end = parts[1] as Long
            
            (start..end).each { Long id ->
                if (isInvalid(id)) {
                    invalidIds.add(id)
                }
            }
        }

        Long sum = invalidIds.sum()
        println "Sum of invalid IDs: ${sum}"

        expect:
        sum == 19605500130
    }
}