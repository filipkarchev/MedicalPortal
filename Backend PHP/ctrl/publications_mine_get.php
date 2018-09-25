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
$user_id=$_POST["user_id"];

}  catch (Exception $e){
    isokFalse("Wrong post data");
    die();
}

try{
    
     $stmt = $db->prepare("SELECT publications.id,publications.creator_id,publications.title,publications.description,publications.seen,publications.data,publications.votes,publications.category
FROM medical_portal_backend.publications 
left join medical_portal_backend.comments 
on(publications.id = comments.publication_id)
where publications.creator_id = $user_id or comments.creator_id = $user_id 
group by publications.id;");
     $stmt->execute();
   

    $info = $stmt->fetchAll(PDO::FETCH_ASSOC);

isokTrue($info);

} catch(PDOException $ex) {
    //Something went wrong rollback!
   // $db->rollBack();
    isokFalse($ex->getMessage());
}
