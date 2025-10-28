from fastapi import FastAPI
from lunar_python import Solar
from pydantic import BaseModel

app = FastAPI()

class UserRequest(BaseModel):
    birth: str      # "2000/01/01 01:23"
    calendar: str

# 오행 계산 함수
def calculate_wuxing(eight_char):
    gans = [
        eight_char.getYearGan(),
        eight_char.getMonthGan(),
        eight_char.getDayGan(),
        eight_char.getTimeGan()
    ]
    zhis = [
        eight_char.getYearZhi(),
        eight_char.getMonthZhi(),
        eight_char.getDayZhi(),
        eight_char.getTimeZhi()
    ]

    gan_to_wuxing = {
        "甲": "wood", "乙": "wood",
        "丙": "fire", "丁": "fire",
        "戊": "earth", "己": "earth",
        "庚": "metal", "辛": "metal",
        "壬": "water", "癸": "water"
    }

    zhi_to_wuxing = {
        "子": "water",
        "丑": "earth", "寅": "wood", "卯": "wood",
        "辰": "earth", "巳": "fire", "午": "fire", "未": "earth",
        "申": "metal", "酉": "metal", "戌": "earth", "亥": "water"
    }

    wuxing_count = {"wood":0, "fire":0, "earth":0, "metal":0, "water":0}

    for g in gans:
        if g in gan_to_wuxing:
            wuxing_count[gan_to_wuxing[g]] += 1

    for z in zhis:
        if z in zhi_to_wuxing:
            wuxing_count[zhi_to_wuxing[z]] += 1

    return wuxing_count

# FastAPI 엔드포인트
@app.post("/analyze")
def analyze_oheng(data: UserRequest):
    date_part, time_part = data.birth.split(' ')
    year, month, day = map(int, date_part.split('/'))
    hour, minute = map(int, time_part.split(':'))

    solar = Solar.fromYmdHms(year, month, day, hour, minute, 0)     # 정수 입력
    lunar = solar.getLunar()    # 음력 변환
    eight_char = lunar.getEightChar()   # 천간, 지지

    wuxing = calculate_wuxing(eight_char)

    strength = max(wuxing, key=wuxing.get)
    weakness = min(wuxing, key=wuxing.get)

    result = {
        # 천간, 지지 분석
        "saju": {
            "year": {"gan": eight_char.getYearGan(), "zhi": eight_char.getYearZhi()},
            "month": {"gan": eight_char.getMonthGan(), "zhi": eight_char.getMonthZhi()},
            "day": {"gan": eight_char.getDayGan(), "zhi": eight_char.getDayZhi()},
            "hour": {"gan": eight_char.getTimeGan(), "zhi": eight_char.getTimeZhi()}
        },
        "five_elements": wuxing,    # 오행 개수 세기
        "balance": {
            "strength": strength,   # 최다 오행 종류
            "weakness": weakness    # 최소 오행 종류
        }
    }
    return result