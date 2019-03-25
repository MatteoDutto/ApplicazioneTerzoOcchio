import socket
import mysql.connector
from threading import Thread

#request: LOGIN "login--mail-|-psw"
#response: LOGIN "Rlogin--idLuogo-nome-indirizzo-latitudine-longitudine-|-"

def user_login(request, cursor):

	auth = False
	data = request.split("-|-")
	userMail = data[0]
	userPsw = data[1]
	sql = "SELECT id, password FROM utente WHERE email = %s"
	values = (userMail,)
	dbRet = cursor.execute(sql, values)
	dbRet = cursor.fetchall()

	pswDb = dbRet[0][1]
	if userPsw == pswDB:
		auth = True

	userId = dbRet[0][0]
	sql = "SELECT idLuogo, nome, indirizzo, latitudine, longitudine FROM luoghi WHERE idUtente = %s "
	values = (userId,)
	dbRet = cursor.execute(sql, values)
	dbRet = cursor.fetchall()

	response = "Rlogin--"
	for record in dbRet:
		response += str(record[0]) + "-"
		response += record[1] + "-" 
		response += record[2] + "-"
		response += str(record[3]) + "-" 
		response += str(record[4]) + "-|-"

	
def single_thread(conn, addr, db):

	cursor = db.cursor()
	request = conn.recv(1024)

	while request != "exit":
		
		if request == "login":
			response = user_login(request, cursor)

		elif request == "pointInfo":
			response = 

		elif request == "choosePath":
			response = 

		else:
			response = "error"

		conn.send(response)
		request = conn.recv(1024)


#initial values
ip = ""
port = 0
host = ""
user = ""
password = ""
dbName = ""

#initialization - db
db = mysql.connector.connect(host, user, password, dbName)


#initialization - socket
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.bind((ip, port))
sock.listen(5)

try:

	while 1:

		conn, addr = sock.accept()
		#try
		Thread(target=client_thread, args=(conn, addr, db)).start()




except KeyboardInterrupt:
	print("Closing...")

finally:
	sock.close()







