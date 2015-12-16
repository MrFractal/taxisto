#import "ClientInfo.h"
#import "DeviceInfoModel.h"

/**
 * Created by max on 06.02.14.
 */

@interface RegistrationInfoRequest : NSObject {
  ClientInfo * clientInfo;
  DeviceInfoModel * deviceInfoModel;
}

@property(nonatomic, retain) ClientInfo * clientInfo;
@property(nonatomic, retain) DeviceInfoModel * deviceInfoModel;
- (void) setClientInfo:(ClientInfo *)clientInfo;
- (void) setDeviceInfoModel:(DeviceInfoModel *)deviceInfoModel;
@end
