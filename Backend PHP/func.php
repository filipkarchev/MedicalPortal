<?php

function isokFalse($msg) {
    echo json_encode(($msg) ? ["isok" => FALSE, "msg" => $msg] : ["isok" => FALSE]);
}

function isokTrue($data) {
    echo json_encode(["isok" => TRUE, "data" => $data]);
}
