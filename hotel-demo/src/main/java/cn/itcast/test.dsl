GET _search
{
  "query": {
    "match_all": {}
  }
}

# 使用Dev Tool编写DSL语句操作es
# 模拟请求
Get /

# 检测默认分词器（向es发请求，es对用户输入数据进行分词）
# 默认分词器，对于中文不够友好，将每个字分为一个词
POST /_analyze
{
  "analyzer": "standard",
  "text": "基础入门Java"
}


# IK分词器有两种模式
# ik_smart：最少切分
# ik_max_word：最细切分（切分为一个词后，还会继续判断是否仍可以切分）
GET /_analyze
{
  "analyzer": "ik_max_word",
  "text": "黑马程序员学习java太棒了"
}

# 最少切分（切分为一个词后，就不会继续判断是否可以继续切分）
GET /_analyze
{
  "analyzer": "ik_smart",
  "text": "黑马程序员学习java太棒了"
}

# 测试ik分词器扩展功能
# 可以添加词库（便于分词）
# 可以停止一些词的分词（比如敏感词汇，不参与分词）
GET /_analyze
{
  "analyzer": "ik_smart",
  "text": "黑马程序员学习java太棒了，奥里给，白嫖，吗，的，了"
}

# 创建索引库
PUT /heima
{
  "mappings": {
    "properties": {
      "info": {
        "type": "text",
        "analyzer": "ik_smart"
      },
      "email": {
        "type": "keyword",
        "index": false
      },
      "name": {
        "properties": {
          "firstName": {
            "type": "keyword"
          }
        }
      }
    }
  }
}
# 查询索引库
Get /heima
 
# 修改索引库（不能修改原有索引库mapping，只可以添加字段）
PUT /heima/_mapping 
{
  "properties": {
    "age": {
      "type": "integer"
    }
  }
}


# 删除索引库
DELETE /heima


# 新增文档
POST /heima/_doc/1
{
  "info": "我叫吴浩",
  "email": "2945608334@qq.com",
  "name": {
    "firstName": "吴"
  }
  
}

# 查询文档
GET /heima/_doc/1

DELETE /heima/_doc/1

# 修改文档（全量修改，直接覆盖原有文档）
# 如果文档id在索引库中不存在，则为新增文档
PUT /heima/_doc/2
{
  "info": "我叫吴浩浩",
  "email": "2945608334@qq.com",
  "name": {
    "firstName": "哈哈"
  }
  
}

# 增量修改，指定字段进行修改
# 如果改字段，文档中不存在，则为新增字段（修改文档）
POST /heima/_update/1
{
  "doc": {
    "info2": "吴浩浩1111222223333"
  }
}

# 创建索引库
PUT /hotel
{
  "mappings": {
    "properties": {
      "id": {
        "type": "keyword"
      },
      "name": {
        "type": "text",
        "analyzer": "ik_max_word",
        "copy_to": "all"
      },
      "address": {
        "type": "keyword",
        "index": false
      },
      "price": {
        "type": "integer"
      },
      "score": {
        "type": "integer"
      },
      "brand": {
        "type": "keyword",
        "copy_to": "all"
      },
      "city": {
        "type": "keyword",
        "copy_to": "all"
      },
      "startName": {
        "type": "keyword"
      },
      "business": {
        "type": "keyword"
      },
      "location": {
        "type": "geo_point"
      },
      "pic": {
        "type": "keyword",
        "index": false
      },
      "all": {
        "type": "text",
        "analyzer": "ik_max_word"
      }
    }
  }
  
}

GET /hotel

DELETE /hotel

GET /hotel/_doc/1734220581


# "match_all"查询所有，es默认会分页
GET /hotel/_search
{
  "query": {
    "match_all": {
      
    }
    
  }
}

# 全文检索（单字段查询）
GET /hotel/_search
{
  "query": {
    "match": {
      "all": "外滩如家"
    }
  }
}

# 全文检索（多字段查询）(建议使用copy_to单字段查询)
# 搜索字越多，查询效率越低
GET /hotel/_search
{
  "query": {
    "multi_match": {
      "query": "外滩如家",
      "fields": ["brand", "name", "city"]
    }
  }
}

# 精准查询，用户输入字段不会分词，需要和es中数据完全一致
# 查询条件也需要不会分词的字段
GET /hotel/_search
{
  "query": {
    "term": {
      "city": {
        "value": "上海"
      }
    }
  }
}


# ranage查询，范围查询，一般对数值类型数据做过滤时会使用
# 比如做价格的过滤
# gte >=    gt  >
# ite <=    it <
GET /hotel/_search 
{
  "query": {
    "range": {
      "price": {
        "gte": 100,
        "lte": 300
      }
    }
  }
}

# 地理坐标查询(在矩形内的点都可以查询到)
# 需要指定左上角和右下角点，画出一个矩形
GET /hotel/_search
{
  "query": {
    "geo_bounding_box": {
      "location": {
        "top_left": {
          "lat": 31.1,
          "lon": 121.5
        },
        "bottom_right": {
          "lat": 30.89,
          "lon": 121.7
        }
        
      }
    }
  }
}

# 附近查询
# location为圆心，distance为半径，圆内的所有点
GET /hotel/_search 
{
  "query": {
    "geo_distance": {
      "distance": "2km",
      "location": "31.21,121.5"
    }
  }
}

# 复合查询，可指定文档分数，让具体文档排名靠前
# 过滤条件：哪些文档需要加分
# 算分函数：如何计算function score
# 加权方式：funcyion score与query score怎样计算
# query：原始查询
# filter：过滤条件
# weight：指定function score
# boost_mode：指定function score 和 query score怎样计算
GET /hotel/_search 
{
  "query": {
    "function_score": {
      "query": { 
        "match_all": {}
      },
      "functions": [
        {
          "filter": {
            "term": {
              "brand": "如家"
            }
          },
          "weight": 2
        }
      ],
      "boost_mode": "sum"
    }
  }
}

# 布尔查询（多个子查询的组合）
# must 必须匹配的条件，与
# should 选择匹配的条件，或
# must_not 必须不匹配的条件，不参与打分
# filter 必须参与的条件，不参与打分
# 需要打分的字段越多，效率就越差
# 搜索框输入，需要全文检索，使用must，参与打分
# 其他过滤条件使用，filter不参与打分
GET /hotel/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "name": "如家"
          }
        }
      ],
      "must_not": [
        {
          "range": {
            "price": {
              "gt": 400
            }
          }
        }
      ],
      "filter": [
        {
          "geo_distance": {
            "distance": "10km",
            "location": {
              "lat": 31.21,
              "lon": 121.5
            }
          }
        }
      ]
     
    }
  }
}

# 搜索结果处理
# 普通字段排序
# 排序条件是一个数组，可以指定多个条件排序
# 当第一个条件相等时，再按照第二个条件进行排序
GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "score": {
        "order": "asc"
      }
    }
  ]
}


# 经纬度排序
# location：指定坐标
# order：排序方式
# unit：排序的距离单位
GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "_geo_distance": {
        "location": "31.034661, 21.612282",
        "order": "asc",
        "unit": "km"
        
      }
    }
  ]
}

# 分页查询
# es默认只返回前10条数据，根据from和size参数配置分页偏移量和一页中文档的条数
# es在进行分页时，需要将数据全部查询出来，然后进行截取
# 如果分页过深，效率就会比较低
# 即from + size大于10000 es会直接报错
# 深度分页可以采取两种方案：
# search after：分页时需要排序，原理是从上一次的排序值开始，查询下一页数据。官方推荐使用的方式。
# scroll：原理将排序后的文档id形成快照，保存在内存。官方已经不推荐使用。
GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "from": 9900,
  "size": 100,
  "sort": [
    {
      "score": {
        "order": "asc"
      }
    }
  ]
}

# 查询实现高亮
# 高亮是对关键字高亮，因此搜索条件必须带有关键字，而不能是范围这样的查询。
# 默认情况下，高亮的字段，必须与搜索指定的字段一致，否则无法高亮
# 如果要对非搜索字段高亮，则需要添加一个属性：required_field_match=false
GET /hotel/_search
{
  "query": {
    "match": {
      "all": "如家"
    }
  },
  "highlight": {
    "fields": {
      "name": {
        "pre_tags": "<em>",
        "post_tags": "</em>"
      }
    },
    "require_field_match": "false"
  }
}

















