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
$publication_id=$_POST["publication_id"];
$user_id=$_POST["user_id"];
$vote=$_POST["vote"];

$stmt = $db->prepare("SELECT * FROM publication_rating WHERE `user_id` = ? AND `publication_id` = ?");
$stmt->execute(array($user_id,$publication_id));

if($stmt->rowCount()>0)
{
    isokFalse("you have alrady voted");
    die();
}

$stmtInsert = $db->prepare("INSERT INTO publication_rating(user_id, publication_id, vote) VALUES(?, ?, ?)"); 
$stmtInsert->execute(array($user_id,$publication_id,$vote));

$stmtUpdate = $db->prepare("UPDATE publications SET votes = votes + ? WHERE id= ?"); 
$stmtUpdate->execute(array($vote,$publication_id));
//UPDATE user_alerts SET notif = notif + 2 WHERE ( user_id = ? )

isokTrue("success");

} catch(PDOException $ex) {
    //Something went wrong rollback!
   // $db->rollBack();
    isokFalse($ex->getMessage());
}
