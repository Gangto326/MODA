from py_hanspell_aideer import spell_checker

def spell_check(text: str):
    result = spell_checker.check(text).as_dict()

    if result['result']:
        return result['checked']

    return result['original']