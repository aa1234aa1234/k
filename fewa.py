import re
import pymysql
import asyncio
from playwright.sync_api import Playwright, sync_playwright, expect
conn = pymysql.connect(host='pcall.kr',user='chatting',password='coxld1234',db='chatting')
curs = conn.cursor()
login_id = ''
login_pwd= ''

def run(dataset) -> None:
    date = dataset[3].split('-')
    with sync_playwright() as playwright:
        browser = playwright.chromium.launch(headless=False)
        context = browser.new_context()
        page = context.new_page()
        page.goto("https://www.letskorail.com/ebizprd/prdMain.do")
        page.wait_for_load_state('load')
        print(page.locator('a[href="javascript:fn_popClose(\'404\')"]'))
        #page.on("popup",lambda new_page: (print(f"{new_page.url}")))
        def login():
            page.locator('a:has(img[alt="로그인"])').click()
            page.wait_for_selector('#txtMember')
            page.evaluate("""({login_ida,login_pwdb}) => {
                document.querySelector('#txtMember').value = login_ida;
                document.querySelector('#txtPwd').value = login_pwdb;
                document.querySelector('a[href="javascript:Login(1);"]').click();
            }""",{"login_ida" : login_id,"login_pwdb" : login_pwd})
            #input("")
            page.wait_for_load_state('load')
        def bruh():
            #page.wait_for_selector('span[class="point02"]',timeout=5000)
            print('loaded')
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
        def domchange():
            page.evaluate(f"""() => {{
                var a = document.createElement('a').onclick = window['f_close'];
            }}""")
        page.on("load", bruh)
        page.on('popup',lambda a: print("a"))
        page.on('frameattached',lambda a: print("cool"))
        page.on('framedetached', lambda a: print("wowie"))
        page.on('dialog',lambda a: print(a))
        login()
        if login_id is not None:
            return
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
                page.wait_for_load_state('load')
                fwe = page.evaluate("() => !!document.getElementById('btn_next');")
                
                print(fwe)
                woah = page.locator('a').filter(has=page.locator('img[alt="예약하기"]')).nth(0)
                woah.wait_for()
                if fwe is True:
                    raise Exception("lmao")
                
                if woah.count() > 0:
                    print("haha")
                    woah.click()
                    break
                else:
                    few = page.locator('a:has(img[src="/docs/2007/img/common/btn_next.gif"])').click(timeout=10000)
                #aa = page.evaluate("()=>document.querySelector('img[src=\"/docs/2007/img/common/btn_next.gif\"]');")
            except Exception as e:
                print(e)
                #page.locator('a[href="javascript:inqSchedule()"]').click()
                break
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
        curs.execute("SELECT * FROM account;")
        c = curs.fetchone()
        login_id = c[0]
        login_pwd = c[1]
        conn.commit()
        run(row)
