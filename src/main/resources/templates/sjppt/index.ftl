<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title></title>
    <link rel="stylesheet" href="https://www.layuicdn.com/layui-v2.5.7/css/layui.css">
</head>
<body>

<div class="layui-container">
    <div class="layui-row">
        <div class="layui-col-md8">
            <ul>
                <#list list as m>
                    <li>
                        <a href="/sjppt/detail?fileName=${m.path}">${m.name}</a>
                    </li>
                </#list>
            </ul>
        </div>
    </div>

    <script src="https://www.layuicdn.com/layui-v2.5.7/layui.js"></script>
</body>
</html>