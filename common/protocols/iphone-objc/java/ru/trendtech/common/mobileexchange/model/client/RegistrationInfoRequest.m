#import "RegistrationInfoRequest.h"

@implementation RegistrationInfoRequest

@synthesize clientInfo;
@synthesize deviceInfoModel;

- (void) dealloc {
  [clientInfo release];
  [deviceInfoModel release];
  [super dealloc];
}

@end
