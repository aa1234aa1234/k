import re
import pymysql
from playwright.sync_api import Playwright, sync_playwright, expect
conn = pymysql.connect(host='pcall.kr',user='chatting',password='coxld1234',db='chatting')
curs = conn.cursor()

def run(dataset) -> None:
    date = dataset[3].split('-')
    with sync_playwright() as playwright:
        browser = playwright.chromium.launch(headless=False)
        context = browser.new_context()
        page = context.new_page()
        page.goto("https://www.letskorail.com/ebizprd/prdMain.do")
        print(page.locator('a[href="javascript:fn_popClose(\'404\')"]'))
        #page.on("popup",lambda new_page: (print(f"{new_page.url}")))
        def bruh():
            #page.wait_for_selector('span[class="point02"]',timeout=5000)
            lol = page.evaluate("""()=> {
                var a = document.querySelector('span[class=\"point02\"]');
                return a ? a.innerText : null;
            }""")
            if lol is None:
                return
            curs.execute(f"DELETE FROM Ticket;")
            conn.commit()
            curs.execute(f"INSERT INTO stock.kakaoMsg VALUES (0,'010-3782-2292','{lol}',now());")
            conn.commit()
        page.on("load", bruh)
        test = page.frame(url="https://www.letskorail.com/co/common/popupView.do")
        page.locator('a[href="javascript:inqSchedule()"]').click()
        page.evaluate(f"""document.querySelector('input[name="txtGoStart"]').value=\'{dataset[1]}\';""")
        page.evaluate(f"""document.querySelector('input[name="txtGoEnd"]').value=\'{dataset[2]}\';""")
        #aa = page.evaluate("""()=> {
        #    return !!document.querySelector('img[src=\"/docs/2007/img/common/btn_next.gif\"]');
        #}""")
        #print(aa)
        while True:
            try:
                
                fwe = page.evaluate("() => !!document.getElementById('btn_next');")
                print(fwe)
                if fwe is True:
                    print("fewaf")
                    break
                few = page.locator('a:has(img[src="/docs/2007/img/common/btn_next.gif"])').click(timeout=10000)
                #aa = page.evaluate("()=>document.querySelector('img[src=\"/docs/2007/img/common/btn_next.gif\"]');")
            except Exception:
                page.locator('a[href="javascript:inqSchedule()"]').click()
        input("fewa")
        context.close()
        browser.close()
    



curs.execute("SELECT * FROM taskQueue")
row = curs.fetchone()
curs.execute("SELECT * FROM Ticket")
b = curs.fetchone()
if row is not None:
    if b is None:
        curs.execute(f"INSERT INTO Ticket VALUES (0,'{row[1]}','{row[2]}','{row[3]}');")
        conn.commit()
        run(row)
