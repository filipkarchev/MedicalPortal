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
$text=$_POST["text"];
$categories=$_POST["categories"];


}  catch (Exception $e){
    isokFalse("Wrong post data");
    die();
}

try{
    if (empty($_POST[categories])) {
     $stmt = $db->prepare("SELECT publications.id,publications.creator_id,publications.title,publications.description,publications.seen,publications.data,publications.category,publications.votes,
(SELECT count(*) from comments where comments.publication_id = publications.id) as comments 
FROM publications 
Left Join comments 
ON(publications.id = comments.publication_id)
WHERE `description` LIKE ?
group By publications.id; ");
}
    else{
      $stmt = $db->prepare("SELECT publications.id,publications.creator_id,publications.title,publications.description,publications.seen,publications.data,publications.category,publications.votes,
(SELECT count(*) from comments where comments.publication_id = publications.id) as comments 
FROM publications 
Left Join comments 
ON(publications.id = comments.publication_id)
WHERE `description` LIKE ?
 AND `category` IN ($categories)
group By publications.id; ");}
    $stmt->execute(array('%'.$text.'%'));
    
    //Check if there is publications
    if ($stmt->rowCount() == 0) {
        isokFalse("No publications");
        die();}

    $info = $stmt->fetchAll(PDO::FETCH_ASSOC);

isokTrue($info);

} catch(PDOException $ex) {
    //Something went wrong rollback!
   // $db->rollBack();
    isokFalse($ex->getMessage());
}
