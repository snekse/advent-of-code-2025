import spock.lang.Specification

/**
 * https://adventofcode.com/2025/day/2
 */
class Day02Spec extends Specification {
    boolean isInvalid(long id) {
        if (id == 0) {
            return true // no leading zeros
        } else if (id > -11 && id < 11) {
            return false // known valid ids
        }
        
        String idStr = id.toString()
        def digits = idStr.collect { it }

        // if odd number of digits, return false
        if (digits.size() % 2 != 0) {
            return false
        }

        // split digits into two groups
        def halfSize = (digits.size() / 2) as int
        def firstHalf = digits.take(halfSize)
        def secondHalf = digits.drop(halfSize)

        // if first half equals second half, return true
        if (firstHalf == secondHalf) {
            return true
        }

        return false
    }

    long sumInvalidIds(def ids) {
        return ids.findAll { isInvalid(it) }.sum(0)
    }

    Range createRange(String rangeDef) {
        try {
            def splitRange = rangeDef.split("-")
        return (splitRange[0] as long)..(splitRange[1] as long)
        } catch (Exception e) {
            def msg = "Invalid range: $rangeDef"
            println(msg)
            throw new IllegalArgumentException(msg, e)
        }
    }

    def "isInvalid direct test"() {
        expect:
        isInvalid(1212) == true
    }

    def "test isInvalid: #id -> #result"() {
        expect:
        isInvalid(id) == result

        where:
        id       || result
        0        || true  // no leading zeros
        1        || false
        10       || false
        11       || true
        111      || false // odd number of digits
        1010     || true
        1111     || true
    }
    

    def "test sumInvalidIds: #ids -> #result"() {
        expect:
        sumInvalidIds(ids) == result

        where:
        ids                 || result
        [0,1,10,11,1212]    || (0+11+1212)
        [1212]              || 1212
        [1212, 1212]        || (1212+1212)
        [1]                 || 0
        [123123,1]          || 123123
        1..12               || 11
        0..0                || 0
        createRange("1-13") || 11
    }

    def "test createRange: #rangeDef -> #result"() {
        expect:
        createRange(rangeDef) == result

        where:
        rangeDef                || result
        "1-2"                   || (1..2)
        "10-20"                 || (10..20)
        "5959566378-5959623425" || (5959566378..5959623425)
    }

    def "Solve"() {
        def input = new File("src/test/resources/input.txt").text
        def allRanges = input.split(",").collect { createRange(it) }

        expect:
        allRanges.collect {sumInvalidIds(it)}.sum(0) == 19605500130
    }
}