<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title></title>
    <link rel="stylesheet" href="https://www.layuicdn.com/layui-v2.5.7/css/layui.css">
    <style type="text/css">
        .pic {
            padding: 5px;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>

<div class="layui-container">
    <div class="layui-row">
        <#list list as image>
            <div class="layui-col-md3 pic">
                <img lay-src="${path}/${image}" alt="" style="width: 100%;">
                <p>${image}</p>
            </div>
        </#list>
    </div>
</div>

<script src="https://www.layuicdn.com/layui-v2.5.7/layui.js"></script>
<script>
    layui.use('flow', function () {
        var flow = layui.flow;
        //当你执行这样一个方法时，即对页面中的全部带有lay-src的img元素开启了懒加载（当然你也可以指定相关img）
        flow.lazyimg();
    });
</script>
</body>
</html>