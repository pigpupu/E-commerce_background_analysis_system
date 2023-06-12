import axios from '../utils/axios'

// ==================登录=============================
/**
 * 登录
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
export function loginApi(params) {
    return axios.post('/login', params);
}
// ==================获取用户个人信息=============================
/**
 * 获取用户个人信息
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
export function getOwnInfoApi(params) {
    return axios.post('/getOwnInfo', params, {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    });
}
// ==================注册=============================
/**
 * 注册
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
 export function registerApi(params) {
    return axios.post('/register', params);
}
// ==================修改密码=============================
/**
 * 修改密码
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
 export function alterPasswordApi(params) {
    return axios.post('/alterPassword', params);
}
// ==================忘记密码=============================
/**
 * 忘记密码
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
 export function forgetPasswordApi(params) {
    return axios.post('/forgetPassword', params);
}
// ==================忘记后的重置密码=============================
/**
 * 忘记后的重置密码
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
 export function resetPasswordApi(params) {
    return axios.post('/resetPassword', params);
}





// ==================获取所有种类键值对=============================
/**
 * 获取所有种类键值对
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
 export function queryAllCategory(params) {
    return axios.post('/sale/queryAllCategory', params);
}

// ==================销售量之图=============================
/**
 * 销售量之图
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
 export function basicVolumeChart(params) {
    return axios.post('/sale/basicVolumeChart', params);
}

// ==================销售量之表=============================
/**
 * 销售量之表
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
 export function basicVolumePage(params) {
    return axios.post('/sale/basicVolumePage', params);
}

// ==================销售量之top10=============================
/**
 * 销售量之top10
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
 export function Top10VolumeGoodVo(params) {
    return axios.post('/sale/Top10VolumeGoodVo', params);
}

// ==================销售量之地图=============================
/**
 * 销售量之地图
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
 export function basicVolumeMapChart(params) {
    return axios.post('/sale/basicVolumeMapChart', params);
}



// ==================销售额之图=============================
/**
 * 销售额之图
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
 export function basicMoneyChart(params) {
    return axios.post('/sale/basicMoneyChart', params);
}

// ==================销售额之表=============================
/**
 * 销售额之表
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
 export function basicMoneyPage(params) {
    return axios.post('/sale/basicMoneyPage', params);
}

// ==================销售额之top10=============================
/**
 * 销售额之top10
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
 export function Top10MoneyGoodVo(params) {
    return axios.post('/sale/Top10MoneyGoodVo', params);
}

// ==================销售额之地图=============================
/**
 * 销售额之地图
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
 export function basicMoneyMapChart(params) {
    return axios.post('/sale/basicMoneyMapChart', params);
}


// ==================库存量之图=============================
/**
 * 库存量之表
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
 export function stockVolumeChart(params) {
    return axios.post('/stock/stockVolumeChart', params);
}

// ==================入库量之图=============================
/**
 * 入库量之表
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
 export function instockVolumeChart(params) {
    return axios.post('/stock/instockVolumeChart', params);
}

// ==================入库额之图=============================
/**
 * 入库额之表
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
 export function instockMoneyChart(params) {
    return axios.post('/stock/instockMoneyChart', params);
}

// ==================库存量之表=============================
/**
 * 库存量之表
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
 export function stockVolumePage(params) {
    return axios.post('/stock/stockVolumePage', params);
}
// ==================入库量之表=============================
/**
 * 入库量之表
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
 export function instockVolumePage(params) {
    return axios.post('/stock/instockVolumePage', params);
}
// ==================入库量之表=============================
/**
 * 入库量之表
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
 export function instockMoneyPage(params) {
    return axios.post('/stock/instockMoneyPage', params);
}
