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
        page.goto("https://www.letskorail.com/")
        page.on("popup",lambda new_page: (print("new page opened"),new_page.close()))
        page.locator('a[href="javascript:ingSchedule()"]').click()
        context.close()
        browser.close()
    



curs.execute("SELECT * FROM taskQueue")
row = curs.fetchone()
curs.execute("SELECT * FROM Ticket")
b = curs.fetchone()
if row is not None:
    if b is None:
        curs.execute(f"INSERT INTO Ticket VALUES (0,'{row[1]}','{row[2]}','{row[3]}')")
        conn.commit()
        run(row)
