from explorer.explorer import Explorer

if __name__ == '__main__':
    with Explorer() as bot:
        bot.land_first_page()
        bot.register(
            'Fede Valverde',
            'fede.valverde@fmail.com',
            'Password1234'
        )
        bot.reset_password(
            'fede.valverde@fmail.com',
            'Password1234#'
        )
        bot.login(
            'fede.valverde@fmail.com',
            'Password1234#'
        )
        bot.createTodos()
        bot.updateTodo()
        bot.deleteTodo()
        bot.logout()

    input('Press ENTER to close the browser.....')
