def make_category_prompt(content:str):
    return [
        {
            'role': 'system',
            'content': 'Only print out the categories requested by the user, and do not say anything else.'
        },
        {
            'role': 'user',
            'content': f'''Please choose one of the categories from the image and content given and tell me just the categories.
Don't say anything else and just tell me the categories.
['Trends', 'Entertainment', 'Finance', 'Travel', 'Food', 'IT', 'Design', 'Society', 'Health']

{content}
'''
        }
    ]

def make_process_prompt(content: str):
    return [
        {
            'role': 'system',
            'content': f'''당신은 JSON 형식의 데이터를 마크다운 형식으로 변환하는 전문가입니다. 
입력받은 JSON 데이터의 각 객체에서 title은 h2 헤더(##)로, content는 본문 텍스트로 변환하여 출력해주세요.

변환 규칙:
1. title 필드는 '## ' 접두사를 붙여 헤더로 변환
2. content 필드는 일반 텍스트로 출력
3. 각 항목 사이에는 빈 줄 추가
4. 마크다운 문법을 정확히 준수'''
        },
        {
            'role': 'user',
            'content': f'다음 JSON 데이터를 마크다운 형식으로 요약해줘\n\n{content}'
        }
    ]

def make_keywords_content_prompt(content: str):
    return [
        {
            'role': 'system',
            'content': '''다음 규칙을 반드시 따라주세요:
- 주요 개념이나 주제를 대표하는 단어나 구만 선택
- 각 키워드는 1-3단어로 제한
- 불필요한 조사나 부사는 제외
- 키워드는 3-5개로 제한
- 중요도 순서로 나열'''
        },
        {
            'role': 'user',
            'content': f'당신은 텍스트에서 가장 중요한 핵심 키워드를 추출하는 전문가입니다.\n다음 텍스트에서 가장 중요한 핵심 키워드 3-5개를 추출해주세요:\n\n{content}'
        }
    ]