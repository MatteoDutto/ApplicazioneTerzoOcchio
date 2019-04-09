<?php

	$servername = "localhost";
	$username = "root";
	$password = "";
	$database = "terzoocchio";
	
	try {
		$conn = new PDO("mysql:host=$servername;dbname=$database", $username, $password);
		// set the PDO error mode to exception
		$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		echo "Connected successfully";
	}
	catch(PDOException $e){
		echo "Connection failed: " . $e->getMessage();
		echo "<br>";
	}

	$stmt = $conn->prepare("SELECT * FROM luoghi");
	$result = $stmt->execute();

	$luoghi = array();

	while($row = $stmt->fetch()){
		array_push($luoghi, array($row['latitudine'], $row['longitudine']));
	}

	print_r($luoghi);

	$conn = null; 
?>