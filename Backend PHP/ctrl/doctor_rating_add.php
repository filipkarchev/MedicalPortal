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
$doctor_id=$_POST["doctor_id"];
$user_id=$_POST["user_id"];
$vote=$_POST["vote"];

$date = date('Y-m-d H:i:s', time());
$date = date('Y-m-d H:i:s', strtotime($date. ' - 1 days'));

$canVote=false;
}  catch (Exception $e){
    isokFalse("Wrong post data");
    die();
}

try{

$stmt = $db->prepare("SELECT date FROM medical_portal_backend.messages where sender_id = ? AND receiver_id = ? AND date > ?");
$stmt->execute(array($user_id,$doctor_id,$date));


if ($stmt->rowCount() > 0) {
        $canVote = true;
}


$stmt2 = $db->prepare("SELECT * FROM medical_portal_backend.consultations where user_id = ? AND doctor_id = ? AND date < NOW()");
$stmt2->execute(array($user_id,$doctor_id));


if ($stmt2->rowCount() > 0) {
        $canVote = true;
}

     $stmtCheckVoted = $db->prepare("Select * From doctor_rating where user_id = ? AND doctor_id = ?");
     $stmtCheckVoted->execute(array($user_id,$doctor_id));
     
     if ($stmtCheckVoted->rowCount() > 0) {
         isokFalse("already voted");
         die();
}
     
  if($canVote==TRUE)
  {
     $stmtInsrt = $db->prepare("INSERT INTO doctor_rating(`doctor_id`,`user_id`,`vote`) VALUES(?, ?, ?)");
     $stmtInsrt->execute(array($doctor_id,$user_id,$vote));
     
     $rating=0;$rating_count= 0;
     $stmtGet = $db->prepare("SELECT rating,rating_count FROM medical_portal_backend.doctors where id = ?");
//     $stmtGet->bindParam(':rating', $rating, PDO::PARAM_INT);
//     $stmtGet->bindParam(':rating_count', $rating_count, PDO::PARAM_INT);
     $stmtGet->execute(array($doctor_id));
     $result = $stmtGet->fetch();
     $rating =  $result["rating"];
     $rating_count = $result["rating_count"];
     
     
     //calculate new rating
     $newRating = (($rating * $rating_count) + $vote)/($rating_count + 1);
     
     $stmtUpdate = $db->prepare("UPDATE `medical_portal_backend`.`doctors` SET `rating`=?,`rating_count`=? WHERE `id`=?");
     $stmtUpdate->execute(array($newRating,$rating_count + 1,$doctor_id));
     
     isokTrue($newRating);
     
     
  }
  else
  {
      isokFalse("cannot vote");
  }



} catch(PDOException $ex) {
    //Something went wrong rollback!
   // $db->rollBack();
    isokFalse($ex->getMessage());
}
