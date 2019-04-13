<?php

	$servername = "localhost";
	$username = "root";
	$password = "";
	$database = "my_fiumeeitaliana";
	
	try {
		$conn = new PDO("mysql:host=$servername;dbname=$database", $username, $password);
		// set the PDO error mode to exception
		$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		//echo "Connected successfully";

		$stmt = $conn->prepare("SELECT * FROM luoghi");
		$result = $stmt->execute();
	
		$luoghi = "";
	
		while($row = $stmt->fetch()){
			$luoghi .= $row['latitudine'] . "," . $row['longitudine'] . ";";
		}
	
		echo $luoghi;
		
	}	catch(PDOException $e){
		echo "Connection failed: " . $e->getMessage() . "<br>";
	}

	$conn = null; 
?>