#import "MissionInfo.h"

@implementation MissionInfo

@synthesize id;
@synthesize timeOfRequesting;
@synthesize timeOfAssigning;
@synthesize timeOfArriving;
@synthesize timeOfSeating;
@synthesize timeOfFinishing;
@synthesize timeOfPayment;
@synthesize expectedArrivalTimes;
@synthesize rating;
@synthesize addressFrom;
@synthesize addressTo;
@synthesize locationFrom;
@synthesize locationTo;
@synthesize paymentType;
@synthesize price;
@synthesize driverInfo;
@synthesize clientInfo;
@synthesize autoType;
@synthesize comment;
@synthesize options;
@synthesize fixedMission;

- (void) init {
  if (self = [super init]) {
    expectedArrivalTimes = [[[NSMutableArray alloc] init] autorelease];
    options = [[[NSMutableArray alloc] init] autorelease];
  }
  return self;
}

- (void) dealloc {
  [timeOfRequesting release];
  [timeOfAssigning release];
  [timeOfArriving release];
  [timeOfSeating release];
  [timeOfFinishing release];
  [timeOfPayment release];
  [expectedArrivalTimes release];
  [options release];
  [addressFrom release];
  [addressTo release];
  [locationFrom release];
  [locationTo release];
  [comment release];
  [driverInfo release];
  [clientInfo release];
  [super dealloc];
}

@end
