<template>
    <div id="cnmap" class="container" style="width: 1000px; height: 400px"> </div>
</template>

<script>

import * as echarts from "echarts";
import chinaJson from "echarts/map/js/china.js"

export default {
    name: "cnmap",
    props: {
        mapdata: {
            type: Array,
            default() {
                return [{ name: '上海', value: 0 }, { name: '北京', value: 0 }]
            }
        },
        titleText:{
            type:String
        }
    },
    data() {
        return {
            nowMapData:this.mapdata
        }
    },
    watch: {
        mapdata:function (newVal,oldVal) {
            this.mymapdata = newVal;
			this.drawMap();
        }
    },
    mounted() {
        this.drawMap();
    },
    methods: {
        drawMap() {
            var maxi = 10000;
            var mini = 0;
            var option = {
                title: {
                    text: this.titleText,
                    left: "right",
                },
                tooltip: {
                    trigger: "item",
                    showDelay: 0,
                    transitionDuration: 0.2,
                },
                visualMap: {
                    left: "right",
                    min: mini,
                    max: maxi,
                    inRange: {
                        color: [
                            "#313695",
                            "#4575b4",
                            "#74add1",
                            "#abd9e9",
                            "#e0f3f8",
                            "#ffffbf",
                            "#fee090",
                            "#fdae61",
                            "#f46d43",
                            "#d73027",
                            "#a50026",
                        ],
                    },
                    text: ["High", "Low"],
                    calculable: true,
                },
                toolbox: {
                    show: true,
                    //orient: 'vertical',
                    left: "left",
                    top: "top",
                    feature: {
                        dataView: { readOnly: false },
                        restore: {},
                        saveAsImage: {},
                    },
                },
                series: [
                    {
                        name: "the commercial indicator",
                        type: "map",
                        roam: true,
                        map: "china",
                        emphasis: {
                            label: {
                                show: true,
                            },
                        },
                        data: this.mapdata
                    },
                ],
            };
            echarts.registerMap("China", chinaJson);
            const chartObj = echarts.init(document.getElementById('cnmap'))
            chartObj.setOption(option);
        }
    }
}


</script>

<style scoped>

</style>
