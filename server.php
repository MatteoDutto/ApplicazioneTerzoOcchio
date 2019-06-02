<?php
	$servername = "localhost";
	$username = "root";
	$password = "";
	$database = "my_fiumeeitaliana";

	try {
		$type = $_GET['TYPE'];
		$return = '';

		if($type == 'login'){
			/*
			REQUEST:
			TYPE=login&MAIL=<mail>
			ANSWER:
			
			*/
			$conn = new PDO("mysql:host=$servername;dbname=$database", $username, $password);
			$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

			$mail = $_GET["MAIL"];
			$stmt = $conn->prepare("SELECT password FROM utente WHERE email = :mail ");
			$stmt->bindParam(':mail', $mail);
			$stmt->execute();
			$result = $stmt->fetchAll(PDO::FETCH_COLUMN);

			$return = 'PSW=' . $result[0];

		} elseif ($type == 'coordinate'){
			/*
			REQUEST:
			TYPE=coordinate
			ANSWER:
			
			*/
			$conn = new PDO("mysql:host=$servername;dbname=$database", $username, $password);
			$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

			$stmt = $conn->prepare("SELECT idLuogo, latitudine, longitudine FROM luoghi");
			$stmt->execute();
			$result = $stmt->fetchAll();
			$return = 'COORD=';

			foreach($result as $row){
				$return  = $return . $row[0] . ',' . $row[1] . ',' . $row[2] . ';';
			}

		} elseif ($type == 'qrcode'){
			/*
			REQUEST:
			TYPE=qrcode&PAGINA=<nPagina>
			ANSWER:
			
			*/
			$conn = new PDO("mysql:host=$servername;dbname=$database", $username, $password);
			$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

			$pagina = (int)$_GET["PAGINA"];
			$stmt = $conn->prepare("SELECT titolo, categoria, periodo, testo FROM pagine WHERE idPag = :pagina ");
			$stmt->bindParam(':pagina', $pagina);
			$stmt->execute();
			$result = $stmt->fetchAll();
			
			$return = 'PAGINA=' . $result[0][0] . '__' . $result[0][1] . '__' . $result[0][2] . '__' . $result[0][3] . '___';

		}elseif ($type == 'register'){
			/*
			REQUEST:
			TYPE=register&ID=&NOME=&COGNOME=&DATANASCITA=&EMAIL=&PASSWORD
			ANSWER:
			
			*/
			$conn = new PDO("mysql:host=$servername;dbname=$database", $username, $password);
			$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

		
		}
		echo $return;
		
		
	}	catch(PDOException $e){
		echo "Connection failed: " . $e->getMessage() . "<br>";
	}
	$conn = null; 
?>
