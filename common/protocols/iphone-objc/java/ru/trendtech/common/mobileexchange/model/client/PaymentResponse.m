#import "PaymentResponse.h"

@implementation PaymentResponse

@synthesize payment;
@synthesize missionInfo;
@synthesize paymentInfo;
@synthesize payment;

- (void) init {
  if (self = [super init]) {
    payment = NO;
  }
  return self;
}

- (void) dealloc {
  [missionInfo release];
  [paymentInfo release];
  [super dealloc];
}

@end
