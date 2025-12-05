import spock.lang.Specification

/**
 * https://adventofcode.com/2025/day/2
 */
class Day02Spec extends Specification {
    boolean isInvalidPart1(Long id) {
        String s = id.toString()
        if (s.length() % 2 != 0) {
            return false
        }
        String firstHalf = s.substring(0, s.length() / 2 as int)
        String secondHalf = s.substring(s.length() / 2 as int)
        return firstHalf == secondHalf
    }

    boolean isInvalidPart2(Long id) {
        String s = id.toString()
        int n = s.length()
        // Try all possible lengths for the repeated sequence, from 1 up to n/2
        for (int len = 1; len <= n / 2; len++) {
            if (n % len == 0) {
                String pattern = s.substring(0, len)
                boolean matches = true
                for (int i = len; i < n; i += len) {
                    if (s.substring(i, i + len) != pattern) {
                        matches = false
                        break
                    }
                }
                if (matches) {
                    return true
                }
            }
        }
        return false
    }

    def "Check invalid logic Part 1"() {
        expect:
        isInvalidPart1(id) == expected

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

    def "Check invalid logic Part 2"() {
        expect:
        isInvalidPart2(id) == expected

        where:
        id           | expected
        12341234     | true  // 1234 two times
        123123123    | true  // 123 three times
        1212121212   | true  // 12 five times
        1111111      | true  // 1 seven times
        11           | true
        22           | true
        99           | true
        111          | true  // 1 three times. Part 1: false, Part 2: true
        999          | true
        565656       | true  // 56 three times
        824824824    | true  // 824 three times
        2121212121   | true  // 21 five times
        123          | false
        101          | false
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
                if (isInvalidPart1(id)) {
                    invalidIds.add(id)
                }
            }
        }

        Long sum = invalidIds.sum()
        println "Sum of invalid IDs: ${sum}"

        expect:
        sum == 19605500130
    }

    def "Solve Part 2"() {
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
                if (isInvalidPart2(id)) {
                    invalidIds.add(id)
                }
            }
        }

        Long sum = invalidIds.sum()
        println "Sum of invalid IDs Part 2: ${sum}"

        expect:
        sum == 36862281418
    }
}