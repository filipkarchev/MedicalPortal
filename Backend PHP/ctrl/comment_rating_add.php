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
$comment_id=$_POST["comment_id"];
$user_id=$_POST["user_id"];
$vote=$_POST["vote"];

$stmt = $db->prepare("SELECT * FROM comment_rating WHERE `user_id` = ? AND `comment_id` = ?");
$stmt->execute(array($user_id,$comment_id));

if($stmt->rowCount()>0)
{
    isokFalse("you have alrady voted");
    die();
}

$stmtInsert = $db->prepare("INSERT INTO comment_rating(user_id, comment_id, vote) VALUES(?, ?, ?)"); 
$stmtInsert->execute(array($user_id,$comment_id,$vote));

$stmtUpdate = $db->prepare("UPDATE comments SET votes = votes + ? WHERE id= ?"); 
$stmtUpdate->execute(array($vote,$comment_id));

isokTrue("success");

} catch(PDOException $ex) {
    //Something went wrong rollback!
   // $db->rollBack();
    isokFalse($ex->getMessage());
}
