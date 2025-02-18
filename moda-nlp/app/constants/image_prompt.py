from typing import List

def make_analyze_prompt(base64_image: List[str]):
    return [
        {
            'role': 'system',
            'content': '''You are a professional image analyst.
Please provide a detailed and objective analysis of images according to the following criteria:

1. Key Elements Analysis
   - Main subject/focal point
   - Supporting elements
   - Relationships between elements

2. Textual Elements (if present)
   - Content of text
   - Typography and font styles
   - Text-image integration
   - Readability and hierarchy

Please analyze this using simple, easy-to-understand language.
Explain everything clearly so anyone can follow along, while still pointing out both the obvious features and smaller details that make the image interesting and effective.'''
        },
        {
            'role': 'user',
            'content': 'What do you see in this image?',
            'images': base64_image
        }
    ]

def make_category_prompt(content: str, base64_image: List[str], exclude:List[str]):
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
{"Do Not choose in {exclude}" if len(exclude) != 0 else ""}

{content}''',
            'images': base64_image
        }
    ]

def make_keywords_content_prompt(content: str, base64_image: List[str]):
    return [
        {
            'role': 'system',
            'content': '''Make sure to follow the following rules:
- Choose only words or phrases that represent a key concept or topic
- Each keyword is limited to 1-3 words
- Excluding unnecessary investigations or adverbs
- Restricted to 3-5 keywords
- List in order of importance'''
        },
        {
            'role': 'user',
            'content': f'''You are an expert at extracting the most important key keywords from images.
Please extract the 3-5 most important key keywords from the following image and content

{content}''',
            'images': base64_image
        }
    ]