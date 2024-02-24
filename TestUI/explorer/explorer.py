import os
import random
import time

from selenium import webdriver
import explorer.constants as const

from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys


class Explorer(webdriver.Chrome):
    def __init__(self, driver_path=r"C:\selenium_drivers", teardown=False):
        self.driver_path = driver_path
        self.teardown = teardown
        os.environ['PATH'] += self.driver_path
        super(Explorer, self).__init__()

    def __exit__(self, exc_type, exc_val, exc_tb):
        if self.teardown:
            self.quit()

    def land_first_page(self):
        self.get(const.BASE_URL)
        self.maximize_window()

    def register(self, fullname, email, password):
        WebDriverWait(self, 30).until(
            EC.url_to_be(const.BASE_URL + '/login')
        )
        link = self.find_element(By.CSS_SELECTOR, 'a[routerlink="/register"]')
        link.click()
        WebDriverWait(self, 30).until(
            EC.url_to_be(const.BASE_URL + '/register')
        )
        fullname_input = self.find_element(
            By.CSS_SELECTOR,
            'input[formcontrolname="fullname"]'
                                           )
        fullname_input.send_keys(fullname)
        email_input = self.find_element(
            By.CSS_SELECTOR,
            'input[formcontrolname="email"]'
                                        )
        email_input.send_keys(email)
        password_input = self.find_element(
            By.CSS_SELECTOR,
            'input[formcontrolname="password"]'
        )
        password_input.send_keys(password)
        confirm_pass_input = self.find_element(
            By.CSS_SELECTOR,
            'input[formcontrolname="confirmPass"]'
        )
        confirm_pass_input.send_keys(password)
        register_btn = self.find_element(
            By.CSS_SELECTOR,
            'p-button[type="submit"]'
        )
        register_btn.click()

    def reset_password(self, email, new_pass):
        # Forgot Password
        WebDriverWait(self, 30).until(
            EC.url_to_be(const.BASE_URL + '/login')
        )
        link = self.find_element(
            By.CSS_SELECTOR,
            'a[routerlink="/forgot-password"]'
        )
        link.click()
        WebDriverWait(self, 30).until(
            EC.url_to_be(const.BASE_URL + '/forgot-password')
        )
        email_input = self.find_element(
            By.CSS_SELECTOR,
            'input[formcontrolname="email"]'
        )
        email_input.send_keys(email)
        confirm_btn = self.find_element(
            By.CSS_SELECTOR,
            'p-button[type="submit"]'
        )
        confirm_btn.click()

        # Reset the password
        WebDriverWait(self, 30).until(
            EC.url_to_be(const.BASE_URL + '/reset-password')
        )
        new_pass_inp = self.find_element(
            By.CSS_SELECTOR,
            'input[formcontrolname="newPass"]'
        )
        new_pass_inp.send_keys(new_pass)
        confirm_pass_inp = self.find_element(
            By.CSS_SELECTOR,
            'input[formcontrolname="confirmPass"]'
        )
        confirm_pass_inp.send_keys(new_pass)
        reset_btn = self.find_element(
            By.CSS_SELECTOR,
            'p-button[type="submit"]'
        )
        reset_btn.click()

    def login(self, email, password):
        WebDriverWait(self, 30).until(
            EC.url_to_be(const.BASE_URL + '/login')
        )
        email_inp = self.find_element(
            By.CSS_SELECTOR,
            'input[formcontrolname="email"]'
        )
        email_inp.send_keys(email)
        password_inp = self.find_element(
            By.CSS_SELECTOR,
            'input[formcontrolname="password"]'
        )
        password_inp.send_keys(password)
        login_btn = self.find_element(
            By.CSS_SELECTOR,
            'p-button[type="submit"]'
        )
        login_btn.click()

    def logout(self):
        WebDriverWait(self, 30).until(
            EC.url_to_be(const.BASE_URL + '/')
        )
        logout_icon = self.find_element(
            By.CSS_SELECTOR,
            'i.pi.pi-sign-out'
        )
        logout_icon.click()
        yes_btn = self.find_element(
            By.CSS_SELECTOR,
            'button[ng-reflect-label="Yes"]'
        )
        yes_btn.click()


    def createTodos(self):
        WebDriverWait(self, 30).until(
            EC.url_to_be(const.BASE_URL + '/')
        )
        add_btn = self.find_element(
            By.CSS_SELECTOR,
            'p-button[label="Add Todo"]'
        )
        for i in range(5):
            time.sleep(1)
            name = 'The todo number ' + str(i+1)
            status = random.randint(0, 100)
            add_btn.click()
            name_inp = self.find_element(
                By.CSS_SELECTOR,
                'input[formcontrolname="name"]'
            )
            name_inp.send_keys(name)
            status_inp = self.find_element(
                By.CSS_SELECTOR,
                'input[formcontrolname="status"]'
            )
            if i == 0:
                status_inp.send_keys(Keys.ARROW_RIGHT * status)
            else:
                status_inp.send_keys(Keys.ARROW_LEFT * 50)
                status_inp.send_keys(Keys.ARROW_RIGHT * status)
            create_btn = self.find_element(
                By.CSS_SELECTOR,
                'p-button[ng-reflect-label="Create Todo"]'
            )
            create_btn.click()

    def updateTodo(self):
        WebDriverWait(self, 30).until(
            EC.url_to_be(const.BASE_URL + '/')
        )
        edit_btn = self.find_elements(
            By.CSS_SELECTOR,
            'div.edit-btn'
        )[0]
        name = 'This is the edited todo'
        status = random.randint(0, 100)
        edit_btn.click()
        name_inp = self.find_element(
            By.CSS_SELECTOR,
            'input[formcontrolname="name"]'
        )
        name_inp.send_keys(Keys.BACKSPACE * 100)
        name_inp.send_keys(name)
        status_inp = self.find_element(
            By.CSS_SELECTOR,
            'input[formcontrolname="status"]'
        )
        status_inp.send_keys(Keys.ARROW_LEFT * 100)
        status_inp.send_keys(Keys.ARROW_RIGHT * status)
        update_btn = self.find_element(
            By.CSS_SELECTOR,
            'p-button[ng-reflect-label="Update Todo"]'
        )
        update_btn.click()

    def deleteTodo(self):
        WebDriverWait(self, 30).until(
            EC.url_to_be(const.BASE_URL + '/')
        )
        delete_btn = self.find_elements(
            By.CSS_SELECTOR,
            'div.delete-btn'
        )[3]
        delete_btn.click()
        yes_btn = self.find_element(
            By.CSS_SELECTOR,
            'button[ng-reflect-label="Yes"]'
        )
        yes_btn.click()
