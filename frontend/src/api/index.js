import request from '@/utils/request'

// 认证相关
export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

export function changePassword(userId, data) {
  return request({
    url: `/auth/password?userId=${userId}`,
    method: 'post',
    data
  })
}

// 首页统计
export function getDashboardData() {
  return request({
    url: '/dashboard',
    method: 'get'
  })
}

// 医院管理
export function getHospitals(params) {
  return request({
    url: '/hospitals',
    method: 'get',
    params
  })
}

export function getAllHospitals() {
  return request({
    url: '/hospitals/all',
    method: 'get'
  })
}

export function getHospitalByCode(cohortCode) {
  return request({
    url: `/hospitals/${cohortCode}`,
    method: 'get'
  })
}

export function createHospital(data) {
  return request({
    url: '/hospitals',
    method: 'post',
    data
  })
}

export function updateHospital(cohortCode, data) {
  return request({
    url: `/hospitals/${cohortCode}`,
    method: 'put',
    data
  })
}

export function deleteHospital(cohortCode) {
  return request({
    url: `/hospitals/${cohortCode}`,
    method: 'delete'
  })
}

// 用户管理
export function getUsers(params) {
  return request({
    url: '/users',
    method: 'get',
    params
  })
}

export function listUsers(cohortCode, roleType) {
  return request({
    url: '/users/list',
    method: 'get',
    params: { cohortCode, roleType }
  })
}

export function getUserById(id) {
  return request({
    url: `/users/${id}`,
    method: 'get'
  })
}

export function createUser(data) {
  return request({
    url: '/users',
    method: 'post',
    data
  })
}

export function updateUser(id, data) {
  return request({
    url: `/users/${id}`,
    method: 'put',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: `/users/${id}`,
    method: 'delete'
  })
}

export function assignPermissions(data) {
  return request({
    url: '/users/permissions',
    method: 'post',
    data
  })
}

export function updateProfile(userId, data) {
  return request({
    url: `/users/profile/${userId}`,
    method: 'put',
    data
  })
}

// 患者管理
export function getPatients(params) {
  return request({
    url: '/patients',
    method: 'get',
    params
  })
}

export function getPatientById(id) {
  return request({
    url: `/patients/${id}`,
    method: 'get'
  })
}

export function createPatient(data) {
  return request({
    url: '/patients',
    method: 'post',
    data
  })
}

export function updatePatient(id, data) {
  return request({
    url: `/patients/${id}`,
    method: 'put',
    data
  })
}

export function deletePatient(id) {
  return request({
    url: `/patients/${id}`,
    method: 'delete'
  })
}

export function transferPatient(patientId, data) {
  return request({
    url: `/patients/${patientId}/transfer`,
    method: 'post',
    data
  })
}

// 预警管理
export function getWarnings(params) {
  return request({
    url: '/warnings',
    method: 'get',
    params
  })
}

export function getWarningById(id) {
  return request({
    url: `/warnings/${id}`,
    method: 'get'
  })
}

export function processWarning(id, processorId, processRemark) {
  return request({
    url: `/warnings/${id}/process`,
    method: 'post',
    params: { processorId, processRemark }
  })
}

// 权限管理
export function getPermissionTree() {
  return request({
    url: '/permissions/tree',
    method: 'get'
  })
}

export function getPermissionById(id) {
  return request({
    url: `/permissions/${id}`,
    method: 'get'
  })
}

// 量表题目管理
export function getScaleQuestions(params) {
  return request({
    url: '/scale-questions',
    method: 'get',
    params
  })
}

export function getScaleQuestionById(id) {
  return request({
    url: `/scale-questions/${id}`,
    method: 'get'
  })
}

export function createScaleQuestion(data) {
  return request({
    url: '/scale-questions',
    method: 'post',
    data
  })
}

export function updateScaleQuestion(id, data) {
  return request({
    url: `/scale-questions/${id}`,
    method: 'put',
    data
  })
}

export function deleteScaleQuestion(id) {
  return request({
    url: `/scale-questions/${id}`,
    method: 'delete'
  })
}
