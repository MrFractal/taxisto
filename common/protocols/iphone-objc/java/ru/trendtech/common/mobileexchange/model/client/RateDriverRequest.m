#import "RateDriverRequest.h"

@implementation RateDriverRequest

@synthesize driverId;
@synthesize missionId;
@synthesize comment;
@synthesize totalRate;
@synthesize rateOption1;
@synthesize rateOption2;
@synthesize rateOption3;
@synthesize rateOption4;

- (void) dealloc {
  [comment release];
  [super dealloc];
}

@end
