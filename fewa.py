import re
import pymysql
import asyncio
import time
import threading
#import time as datetime
from datetime import datetime,timedelta
from playwright.sync_api import Playwright, sync_playwright, expect
conn = pymysql.connect(host='pcall.kr',user='chatting',password='coxld1234',db='chatting')
curs = conn.cursor()
login_id = ''
login_pwd= ''

def run(dataset) -> None:
    date = dataset[3].split('-')
    def we(a):
            a.accept()
            print(a.message)
    dialog_event = threading.Event()
    with sync_playwright() as playwright:
        browser = playwright.chromium.launch(headless=False)
        context = browser.new_context()
        page = context.new_page()
        page.on('dialog',we)
        page.goto("https://www.letskorail.com/ebizprd/prdMain.do")
        page.wait_for_load_state('load')
        print(page.locator('a[href="javascript:fn_popClose(\'404\')"]'))
        #page.on("popup",lambda new_page: (print(f"{new_page.url}")))
        def login():
            page.locator('a:has(img[alt="로그인"])').click()
            page.wait_for_selector('#txtMember')
            ia = f"document.querySelector('#txtMember').value = '{login_id}'"
            ib = f"document.querySelector('#txtPwd').value = '{login_pwd}'"
            page.evaluate(ia)
            page.evaluate(ib)
            page.locator('a[href=\"javascript:Login(1);\"]').click();
            #input("")
            page.wait_for_load_state('load')
        def bruh():
            try:
                page.wait_for_selector('span[class="point02"]',timeout=5000)
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
            except Exception as e:
                print(e,end=" load event")
                return
        def domchange():
            page.evaluate(f"""() => {{
                var a = document.createElement('a').onclick = window['f_close'];
            }}""")
        
        page.on('popup',lambda a: a.close())
        page.on('frameattached',lambda a: print("cool"))
        page.on('framedetached', lambda a: print("wowie"))
        
        login()
        test = page.frame(url="https://www.letskorail.com/co/common/popupView.do")
        page.locator('a[href="javascript:inqSchedule()"]').click()
        page.wait_for_url('https://www.letskorail.com/ebizprd/EbizPrdTicketPr21111_i1.do')
        page.evaluate(f"""document.querySelector('input[name="txtGoStart"]').value=\'{dataset[1]}\';""")
        page.evaluate(f"""document.querySelector('input[name="txtGoEnd"]').value=\'{dataset[2]}\';""")
        #aa = page.evaluate("""()=> {
        #    return !!document.querySelector('img[src=\"/docs/2007/img/common/btn_next.gif\"]');
        #}""")
        #print(aa)
        
        page.on("load", bruh)
        
        while True:
            try:
                page.wait_for_load_state('load')
                
                
                
                #woah = page.locator('a').filter(has=page.locator('img[alt="예약하기"]')).nth(0)
                #woah.wait_for()
                
                
                #if woah.count() > 0:
                #    print("haha")
                #    woah.click()
                #    page.wait_for_event('dialog')
                #    break
                #else:
                thing = page.locator('table[id="tableResult"]').locator('tr')
                flagthing = True
                print(thing.count())
                for i in range(1,thing.count()):
                    #print(thing.nth(i).locator('td').nth(3).evaluate("a => a.innerHTML").strip().split("<br>")[1],end=" ")
                    btn = thing.nth(i).locator('td').nth(5)
                    if btn.evaluate("a => a.innerHTML") == "-":
                        continue
                    departtime = datetime.strptime(thing.nth(i).locator('td').nth(2).evaluate("a => a.innerHTML").strip().split("<br>")[1],"%H:%M").replace(
                        year=datetime.now().year, month = datetime.now().month, day = datetime.now().day
                    )
                    time = datetime.strptime(thing.nth(i).locator('td').nth(3).evaluate("a => a.innerHTML").strip().split("<br>")[1],"%H:%M").replace(
                        year=datetime.now().year, month = datetime.now().month, day = datetime.now().day
                    )
                    time2 = datetime.strptime(date[4],"%H").replace(
                        year=datetime.now().year, month = datetime.now().month, day = datetime.now().day
                    )
                    if departtime > datetime.now()-timedelta(minutes=20) and departtime < datetime.now()+timedelta(minutes=20):
                        continue
                    if time.hour < datetime.strptime(thing.nth(i).locator('td').nth(2).evaluate("a => a.innerHTML").strip().split("<br>")[1],"%H:%M").hour:
                        time += timedelta(days=1)
                    if time2.hour < int(date[3]):
                        time2 += timedelta(days=1)
                    if time <= time2:
                        flagthing = False
                        btn.locator('a:has(img[alt="예약하기"])').click()
                        frame = page.locator('iframe[title="열차안내"]')
                        page.wait_for_event('dialog')
                        print(departtime)
                        break
                if flagthing is True:
                    few = page.locator('a:has(img[src="/docs/2007/img/common/btn_next.gif"])').click(timeout=10000)
                    page.wait_for_selector('a:has(img[alt="다음"])',timeout=5000)
                    fwe = page.query_selector('a:has(img[alt="다음"])')
                    print(fwe)
                    if fwe is None:
                        #raise Exception("lmao")
                        page.locator('a[href="javascript:inqSchedule()"]').click()
                else:
                    raise Exception("fuck it")
                    break
                #aa = page.evaluate("()=>document.querySelector('img[src=\"/docs/2007/img/common/btn_next.gif\"]');")
            except Exception as e:
                print(e,end="loop")
                break
                
        
        #input("fewa")
        #time.sleep(5)
        
        #page.wait_for_selector('table[class="tbl_h"]')
        page.wait_for_url('https://www.letskorail.com/ebizprd/EbizPrdTicketPr12111_i1.do')
        
        cols = page.locator('table[class="tbl_h"]').nth(1).locator("td")
        wa = "승차일자: i0\n열차번호: i1\n열차종별: i2\n출발역: i3\n출발시각: i4\n도착역: i5\n도착시각: i6\n예약매수: i7\n총결제금액: i8"
        print(cols.count(),end=" cols")
        for i in range(cols.count()):
            a = cols.nth(i).inner_text().replace(" ", "")
            print(i)
            if a:
                wa = wa.replace("i"+str(i),a)
        print(wa)
        curs.execute(f"DELETE FROM Ticket;")
        conn.commit()
        curs.execute(f"INSERT INTO stock.kakaoMsg VALUES (0,'010-3782-2292','{wa}',now());")
        conn.commit()
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
