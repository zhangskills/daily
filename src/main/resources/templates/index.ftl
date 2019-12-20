<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>每日与主同行</title>
    <link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/3.4.1/css/bootstrap.min.css">
    <style type="text/css">
        body{ background:#fff; font-size: 13px; color: #333; line-height: 25px;}
        table{line-height: 22px;}
    </style>
</head>
<body>
<div class="container">
    <div class="row" id="top">
        <div class="col-md-12" style="margin: 20px auto;">

            <h4>当前日期：${dateStr}</h4>

            <div class="btn-toolbar" role="toolbar">
                <div class="btn-group" role="group">
                    <a class="btn btn-primary" href="${lastUrl!}">上一篇</a>
                </div>

                <div class="btn-group" role="group">
                    <a href="#day1" class="btn btn-default">1</a>
                    <a href="#day2" class="btn btn-default">2</a>
                    <a href="#day3" class="btn btn-default">3</a>
                    <a href="#day4" class="btn btn-default">4</a>
                    <a href="#day5" class="btn btn-default">5</a>
                    <a href="#day6" class="btn btn-default">6、7</a>
                </div>
                <div class="btn-group" role="group">
                    <a class="btn btn-primary" href="${nextUrl!}">下一篇</a>
                </div>
            </div>

        </div>
        <div class="col-md-12">
            ${content!}
        </div>
    </div>
    <a href="#top" style="position:fixed;right:1em;bottom:1em;"> 返回顶部 </a>

</div>

<script src="https://cdn.staticfile.org/twitter-bootstrap/3.4.1/js/bootstrap.min.js"></script>
</body>
</html>