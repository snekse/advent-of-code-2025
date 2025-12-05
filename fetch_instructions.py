import urllib.request
import re
import sys
import html as html_lib
import argparse

def fetch_and_format(day):
    url = f"https://adventofcode.com/2025/day/{day}"
    try:
        req = urllib.request.Request(
            url, 
            data=None, 
            headers={
                'User-Agent': 'github.com/snekse/advent-of-code-2025 by user@email.com'
            }
        )
        with urllib.request.urlopen(req) as response:
            html_content = response.read().decode('utf-8')
    except Exception as e:
        print(f"Error fetching URL: {e}", file=sys.stderr)
        sys.exit(1)

    # Extract the main article content
    article_match = re.search(r'<article class="day-desc">(.*?)</article>', html_content, re.DOTALL)
    if not article_match:
        print("Could not find article content.", file=sys.stderr)
        sys.exit(1)
        
    article_html = article_match.group(1)
    
    # Pre-process HTML to Markdown
    txt = article_html
    
    # 1. em -> bold
    txt = re.sub(r'<em>(.*?)</em>', r'**\1**', txt, flags=re.DOTALL)
    
    # 2. pre code -> code block
    txt = re.sub(r'<pre><code>(.*?)</code></pre>', r'\n```\n\1\n```\n', txt, flags=re.DOTALL)
    
    # 3. code -> inline code
    txt = re.sub(r'<code>(.*?)</code>', r'`\1`', txt, flags=re.DOTALL)
    
    # 4. p -> newlines
    txt = re.sub(r'<p>(.*?)</p>', r'\n\n\1\n', txt, flags=re.DOTALL)
    
    # 5. lists
    txt = re.sub(r'<ul>', r'\n', txt)
    txt = re.sub(r'</ul>', r'\n', txt)
    txt = re.sub(r'<li>(.*?)</li>', r'- \1\n', txt, flags=re.DOTALL)
    
    # 6. links
    txt = re.sub(r'<a href="([^"]+)">(.*?)</a>', r'[\2](\1)', txt)
    
    # 7. Headers (h2)
    txt = re.sub(r'<h2>(.*?)</h2>', r'## \1\n', txt, flags=re.DOTALL)
    
    # 8. Unescape
    txt = html_lib.unescape(txt)
    
    # 9. Strip generic tags remaining
    txt = re.sub(r'<.*?>', '', txt) 
    
    # Identify the start (Header) and end (Last question)
    # The header is usually the first line after cleanup
    # The end is the last sentence ending in '?' before any other footer junk.
    # However, since we extracted <article class="day-desc">, it usually contains ONLY the puzzle text.
    # The "To play, please identify yourself..." is usually outside this article or properly separated.
    # Let's verify if we need to trim the end.
    
    # Check if there's any remaining footer text in the article that isn't part of the puzzle.
    # Usually AoC structure is very clean within day-desc.
    
    # Format lines
    lines = [line.strip() for line in txt.split('\n')]
    clean_lines = []
    
    for l in lines:
        if l:
            clean_lines.append(l)
        elif clean_lines and clean_lines[-1] != "":
            clean_lines.append("")
            
    result = "\n".join(clean_lines)
    
    # Ensure Header
    if not result.startswith("##") and not result.startswith("#"):
        # Attempt to find the Day 3 string
        if f"Day {day}" in result:
             # Just prepend ## if it looks like the title
             pass
    
    print(result)

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Fetch AoC instructions')
    parser.add_argument('day', type=int, help='Day number')
    args = parser.parse_args()
    fetch_and_format(args.day)
