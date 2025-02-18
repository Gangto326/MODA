import asyncio

import googletrans


async def detect(text):
    t = googletrans.Translator()

    a = await t.translate(text, src="en", dest="ko")

    return [aa.text for aa in a]



text = ["blue", "red", "black"]

aaa = asyncio.run(detect(text))


print(aaa)