<?php
header("Content-Type: application/json");
require __DIR__ . "/../init.php";


$db = DB::getInstance()->_pdo;

//Check if there are any POST parameters
if (empty($_POST)) {
    isokFalse("No post data");
    die();
}

try{
//Get POST parameters for user_id,user_name and picture_url
$username=$_POST["username"];
$pass=$_POST["password"];
$firstname=$_POST["firstname"];
$lastname=$_POST["lastname"];
$phone=$_POST["phone"];

$speciality=$_POST["speciality"];
$desc =$_POST["description"];
$city =$_POST["city"];
$address=$_POST["address"];
$category=$_POST["category"];
$hospital_name=$_POST["hospital_name"];
$latitude=$_POST["latitude"];
$longitude=$_POST["longitude"];
}  catch (Exception $e){
    isokFalse("Wrong post data");
    die();
}

try{

    $stmt = $db->prepare("INSERT INTO doctors(`username`,`password`,`firstname`,`lastname`,`phone`,`speciality`,`description`,`city`,`address`,`hospital_name`,`category`,`latitude`,`longitude`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    $stmt->execute(array($username, $pass,$firstname,$lastname,$phone,$speciality,$desc,$city,$address,$hospital_name,$category,$latitude,$longitude));

isokTrue("Doctor created");
   

} catch(PDOException $ex) {
    //Something went wrong rollback!
   // $db->rollBack();
    isokFalse($ex->getMessage());
}

