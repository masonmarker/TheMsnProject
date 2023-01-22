# posts a video to instagram

import requests

id = 'diety.mp4'
token='IGQVJWSmZAMQmtaLTB4UWk3Mm53UDNSMjFqdktncWFzaVVnT0F3NEFmSGVTcXpUWU9DMFBnMU5MMWZAPd3BCbjkzeEZAhNTFBQ0pQeTduQkxpMDRDcVFTd2taUWM1WWR5U3NaeDRkdTh6OWJLTE5uZAFdBTwZDZD'

graph_url = 'https://graph.facebook.com/v15.0/'
def post_reel(caption='', media_type ='',share_to_feed='',thumb_offset='',video_url='',access_token = token,instagram_account_id=id):
    url = graph_url + instagram_account_id + '/media'
    param = dict()
    param['access_token'] = access_token
    param['caption'] = caption
    param['media_type'] = media_type
    param['share_to_feed'] = share_to_feed
    param['thumb_offset'] = thumb_offset
    param['video_url'] = video_url
    response =  requests.post(url,params = param)
    print("\n response",response.content)
    response =response.json()
    return response

############################################ OUTPUT ############################################
# {
#   "id": "17889455560051444"
# }


def status_of_upload(ig_container_id = '',access_token=token):
    url = graph_url + ig_container_id
    param = {}
    param['access_token'] = access_token
    param['fields'] = 'status_code'
    response = requests.get(url,params=param)
    response = response.json()
    return response
####################################### OUTPUT #############################################
# {
#   "status_code": "FINISHED",
#   "id": "17889615691921648"
# }


def publish_container(creation_id = '',access_token =token,instagram_account_id=id):
    url = graph_url + instagram_account_id + '/media_publish'
    param = dict()
    param['access_token'] = access_token
    param['creation_id'] = creation_id
    response = requests.post(url,params=param)
    response = response.json()
    return response
    
################################# OUTPUT #########################################
# {
#   "id": "17920238422030506"
# }

post_reel(caption="aweofijaweofjawe", media_type ='VIDEO',share_to_feed='true',thumb_offset='0.000000',video_url='https://www.youtube.com/watch?v=QH2-TGUlwu4')




